package com.github.nija123098.evelyn.discordobjects;

import com.github.nija123098.evelyn.audio.SpeechParser;
import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import com.github.nija123098.evelyn.chatbot.ChatBot;
import com.github.nija123098.evelyn.command.CommandHandler;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.configs.guild.GuildIgnoredConfig;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.helpers.ReactionBehavior;
import com.github.nija123098.evelyn.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.github.nija123098.evelyn.discordobjects.wrappers.*;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.BotEvent;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventDistributor;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.*;
import com.github.nija123098.evelyn.exception.InvalidEventException;
import com.github.nija123098.evelyn.launcher.Launcher;
import com.github.nija123098.evelyn.moderation.DeletePinNotificationConfig;
import com.github.nija123098.evelyn.moderation.messagefiltering.MessageMonitor;
import com.github.nija123098.evelyn.perms.BotRole;
import com.github.nija123098.evelyn.perms.ContributorMonitor;
import com.github.nija123098.evelyn.template.KeyPhrase;
import com.github.nija123098.evelyn.template.Template;
import com.github.nija123098.evelyn.template.TemplateHandler;
import com.github.nija123098.evelyn.util.*;
import org.apache.http.message.BasicNameValuePair;
import org.reflections.Reflections;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.events.Event;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.api.internal.DiscordEndpoints;
import sx.blah.discord.api.internal.Requests;
import sx.blah.discord.api.internal.json.responses.GatewayBotResponse;
import sx.blah.discord.handle.impl.events.guild.GuildUpdateEvent;
import sx.blah.discord.handle.impl.events.guild.category.CategoryUpdateEvent;
import sx.blah.discord.handle.impl.events.guild.channel.ChannelUpdateEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageUpdateEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionEvent;
import sx.blah.discord.handle.impl.events.guild.member.UserLeaveEvent;
import sx.blah.discord.handle.impl.events.guild.role.RoleUpdateEvent;
import sx.blah.discord.handle.impl.events.guild.voice.user.UserVoiceChannelMoveEvent;
import sx.blah.discord.handle.impl.events.shard.ReconnectSuccessEvent;
import sx.blah.discord.handle.impl.events.shard.ShardReadyEvent;
import sx.blah.discord.handle.impl.events.user.PresenceUpdateEvent;
import sx.blah.discord.handle.impl.events.user.UserUpdateEvent;
import sx.blah.discord.handle.impl.obj.ReactionEmoji;
import sx.blah.discord.handle.obj.ActivityType;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.StatusType;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * Adapts Discord4J to the bot's wrappings.
 *
 * @author nija123098
 * @since 1.0.0
 */
public class DiscordAdapter {
    private static final int MIN_MESSAGE_PARSE_THREADS = Runtime.getRuntime().availableProcessors();
    private static final ThreadPoolExecutor MESSAGE_PARSE_EXECUTOR = new ThreadPoolExecutor(MIN_MESSAGE_PARSE_THREADS, MIN_MESSAGE_PARSE_THREADS * 2, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<>(), r -> ThreadHelper.getDemonThread(r, "Message-Parse"));
    private static final Map<IMessage, Future<?>> MESSAGE_PARSE_FUTURES = new HashMap<>();
    private static final ScheduledExecutorService PLAY_TEXT_EXECUTOR = Executors.newSingleThreadScheduledExecutor(r -> ThreadHelper.getDemonThreadSingle(r, "Play-Text-Changer-Thread"));
    private static final Map<Class<? extends Event>, Constructor<? extends BotEvent>> EVENT_MAP;
    private static final long PLAY_TEXT_SPEED = 60_000, GUILD_SAVE_SPEED = 3_600_000;// 1 hour
    private static final CacheHelper.ContainmentCache<Template> PREVIOUS_TEXTS = new CacheHelper.ContainmentCache<>(PLAY_TEXT_SPEED + 1000);// a second for execution time
    private static final ScheduledExecutorService GUILD_SAVE_EXECUTOR = Executors.newSingleThreadScheduledExecutor(r -> ThreadHelper.getDemonThreadSingle(r, "Guild-Number-Save"));
    public static final AtomicBoolean PLAY_TEXT_UPDATE = new AtomicBoolean();

    static {
        MessageMonitor.init();
        Set<Class<? extends BotEvent>> classes = new Reflections(Launcher.BASE_PACKAGE + ".discordobjects.wrappers.event.events").getSubTypesOf(BotEvent.class);
        classes.remove(DiscordMessageReceived.class);
        EVENT_MAP = new HashMap<>(classes.size() + 2, 1);
        classes.stream().filter(clazz -> !clazz.isAssignableFrom(DiscordMessageReceived.class)).filter(clazz -> !clazz.isAssignableFrom(DiscordUserLeave.class)).filter(clazz -> !clazz.isAssignableFrom(DiscordReactionEvent.class)).filter(clazz -> !clazz.isAssignableFrom(DiscordPresenceUpdate.class)).map(clazz -> clazz.getConstructors()[0]).forEach(constructor -> EVENT_MAP.put((Class<? extends Event>) constructor.getParameterTypes()[0], (Constructor<? extends BotEvent>) constructor));
        ClientBuilder builder = new ClientBuilder();
        builder.withToken(ConfigProvider.BOT_SETTINGS.botToken());
        builder.withMinimumDispatchThreads(1);
        builder.withMaximumDispatchThreads(2);
        builder.setMaxReconnectAttempts(Integer.MAX_VALUE);
        builder.withIdleDispatchThreadTimeout(1, TimeUnit.MINUTES);
        builder.registerListener((IListener<ShardReadyEvent>) event -> event.getShard().changePresence(StatusType.IDLE, ActivityType.WATCHING, "the login screen!"));
        int total = Requests.GENERAL_REQUESTS.GET.makeRequest(DiscordEndpoints.GATEWAY + "/bot", GatewayBotResponse.class, new BasicNameValuePair("Authorization", "Bot " + ConfigProvider.BOT_SETTINGS.botToken()), new BasicNameValuePair("Content-Type", "application/json")).shards;
        List<Integer> list = new ArrayList<>(total);
        for (int i = 0; i < total; i++) if (i % ConfigProvider.BOT_SETTINGS.numberOfShards() == ConfigProvider.BOT_SETTINGS.evelynShardNumber()) list.add(i);
        DiscordClient.set(list.stream().map(integer -> builder.setShard(integer, total)).map(ClientBuilder::login).collect(Collectors.toList()));
        int i = 20 + 25 * DiscordClient.getShardCount();
        for (; i > -1; --i) {
            if (DiscordClient.isReady()) break;
            CareLess.lessSleep(1000);
        }
        if (i == 0) {
            Log.log("Could not load in time");
            System.exit(-1);
        }
        DiscordClient.load();
        GuildAudioManager.init();
        SpeechParser.init();
        ContributorMonitor.init();
        Launcher.registerStartup(() -> {
            DiscordClient.clients().forEach(client -> client.getDispatcher().registerListener(EventDistributor.class));
            DiscordClient.clients().forEach(client -> client.getDispatcher().registerListener(DiscordAdapter.class));
            DiscordClient.clients().forEach(client -> client.getDispatcher().registerListener((IListener<PresenceUpdateEvent>) event -> {
                if (!event.getUser().isBot() && BotRole.BOT_ADMIN.hasRequiredRole(User.getUser(event.getUser()), null)) {
                    event.getNewPresence().getText().ifPresent(s -> {
                        if (s.equalsIgnoreCase("evelyn reboot")) ExecuteShellCommand.commandToExecute(ConfigProvider.BOT_SETTINGS.startCommand(), ConfigProvider.BOT_SETTINGS.botFolder());
                    });
                }
            }));
            EventDistributor.register(ReactionBehavior.class);
            EventDistributor.register(MessageMonitor.class);
        });
        if (!ConfigProvider.BOT_SETTINGS.ghostModeEnabled()) PLAY_TEXT_EXECUTOR.scheduleAtFixedRate(() -> {
            if (!PLAY_TEXT_UPDATE.get()) return;
            Template template = TemplateHandler.getTemplate(KeyPhrase.PLAY_TEXT, null, PREVIOUS_TEXTS.asArrayList());
            if (template == null) {
                TemplateHandler.addTemplate(KeyPhrase.PLAY_TEXT, null, "with nitroglycerine");
                Log.log("Template KeyPhrase for " + KeyPhrase.PLAY_TEXT.name() + " has been added: \"with nitroglycerine\"");
            }
            if (template != null) DiscordClient.getShards().forEach(shard -> shard.changePresence(Presence.Status.ONLINE, Presence.Activity.PLAYING, template.interpret((User) null, shard, null, null, null, null)));
        }, PLAY_TEXT_SPEED + 10_000, PLAY_TEXT_SPEED, TimeUnit.MILLISECONDS);
        Path path = Paths.get(ConfigProvider.RESOURCE_FILES.timeStats());
        File file = path.toFile();
        if (!file.exists()) {
            try{file.createNewFile();
            } catch (IOException e) {
                Log.log("Could not make a file for tracking guild count", e);
            }
        }
        GUILD_SAVE_EXECUTOR.scheduleAtFixedRate(() -> {
            long time = System.currentTimeMillis();
            time -= time % GUILD_SAVE_SPEED;
            while (!DiscordClient.isReady()) CareLess.lessSleep(10_000);
            try{Files.write(path, Collections.singleton(time + " " + DiscordClient.getGuilds().size()), StandardOpenOption.APPEND);
            } catch (IOException e) {
                Log.log("Could not save guild count", e);
            }
        }, System.currentTimeMillis() % GUILD_SAVE_SPEED, GUILD_SAVE_SPEED, TimeUnit.MILLISECONDS);
    }
    /**
     * Forces the initialization of this class.
     */
    public static void initialize() {
        Log.log(LogColor.blue("Discord Adapter initialized.") + LogColor.yellow(" Converting Discord to 240v."));
    }
    @EventSubscriber
    public static void handle(ShardReadyEvent event) {
        event.getShard().changePresence(StatusType.IDLE, ActivityType.PLAYING, "with the loading screen!");
    }
    @EventSubscriber
    public static void handle(ReconnectSuccessEvent event) {
        DiscordClient.changePresence(Presence.Status.ONLINE, Presence.Activity.PLAYING, "with users!");
    }
    @EventSubscriber
    public static void handle(UserUpdateEvent event) {
        User.update(event.getNewUser());
    }
    @EventSubscriber
    public static void handle(GuildUpdateEvent event) {
        Guild.update(event.getNewGuild());
    }
    @EventSubscriber
    public static void handle(RoleUpdateEvent event) {
        Role.update(event.getNewRole());
    }
    @EventSubscriber
    public static void handle(ChannelUpdateEvent event) {
        Channel.update(event.getNewChannel());
    }
    @EventSubscriber
    public static void handle(MessageUpdateEvent event) {
        Message.update(event.getNewMessage());
    }
    @EventSubscriber
    public static void handle(CategoryUpdateEvent event) {
        Category.update(event.getNewCategory());
    }
    @EventSubscriber
    public static void handle(UserVoiceChannelMoveEvent event) {
        EventDistributor.distribute(new DiscordVoiceLeave(event.getOldChannel(), event.getUser()));
        EventDistributor.distribute(new DiscordVoiceJoin(event.getNewChannel(), event.getUser()));
    }
    @EventSubscriber
    public static void handle(ReactionEvent event) {// it's cleaner than the alternative
        EventDistributor.distribute(new DiscordReactionEvent(event));
    }


    private static final Map<IUser, Boolean> MANAGE_PRESENCE_CACHE = new ConcurrentHashMap<>();
    private static boolean shouldManagePresence(IUser iUser) {
        return !iUser.isBot() && User.getUser(iUser).getGuilds().stream().anyMatch(guild -> !ConfigHandler.getSetting(GuildIgnoredConfig.class, guild));
    }
    public static void managePresences(Guild guild) {// added for efficiency when guilds are known to be managed
        guild.getUsers().stream().filter(user -> !user.isBot()).map(User::user).forEach(iUser -> MANAGE_PRESENCE_CACHE.put(iUser, true));
    }
    @EventSubscriber
    private static void handle(UserLeaveEvent event) {
        if (MANAGE_PRESENCE_CACHE.get(event.getUser()) == Boolean.TRUE && !ConfigHandler.getSetting(GuildIgnoredConfig.class, Guild.getGuild(event.getGuild()))) MANAGE_PRESENCE_CACHE.remove(event.getUser());
    }
    @EventSubscriber
    public static void handle(PresenceUpdateEvent event) {
        if (MANAGE_PRESENCE_CACHE.computeIfAbsent(event.getUser(), DiscordAdapter::shouldManagePresence)) EventDistributor.distribute(new DiscordPresenceUpdate(event));
    }

    /**
     * Handles receiving messages from users.
     * First it is processed through {@link MessageMonitor},
     * then invoked if it is read as a command,
     * then distributed to other {@link MessageReceivedEvent} listeners.
     *
     * @param event the Discord4J event to listen for.
     */
    @EventSubscriber
    public static void handle(MessageReceivedEvent event){
        if (event.getAuthor().isBot() || !Launcher.isReady() || event.getMessage().getContent() == null) return;
        MESSAGE_PARSE_FUTURES.put(event.getMessage(), MESSAGE_PARSE_EXECUTOR.submit(() -> {
            if (event.getMessage().getContent().isEmpty()) DeletePinNotificationConfig.handle(new DiscordMessageReceived(event));
            DiscordMessageReceived receivedEvent = new DiscordMessageReceived(event);
            if (MessageMonitor.monitor(receivedEvent)) return;
            Boolean isCommand = CommandHandler.handle(receivedEvent);
            if (isCommand == null){
                String thought = receivedEvent.getMessage().getContent();
                if (ChatBot.mayChat(receivedEvent.getChannel(), receivedEvent.getMessage().getContent())) {
                    new MessageMaker(receivedEvent.getChannel()).appendRaw(ChatBot.getChatBot(receivedEvent.getChannel()).think(thought)).send();
                    receivedEvent.setCommand(true);
                    isCommand = true;
                } else isCommand = false;
            }
            receivedEvent.setCommand(isCommand);
            if (!isCommand && event.getMessage().getMentions().contains(DiscordClient.getOurUser().user()) && !event.getMessage().mentionsEveryone() && !event.getMessage().mentionsHere()) {
                ExceptionWrapper.wrap(() -> event.getMessage().addReaction(ReactionEmoji.of(EmoticonHelper.getChars("eyes", false))));
            }// This does the :eyes: on mention now.
            EventDistributor.distribute(receivedEvent);
            MESSAGE_PARSE_FUTURES.remove(event.getMessage());
            if (MESSAGE_PARSE_FUTURES.size() > MIN_MESSAGE_PARSE_THREADS) {
                new HashMap<>(MESSAGE_PARSE_FUTURES).forEach((iMessage, future) -> {
                    if (System.currentTimeMillis() - iMessage.getCreationDate().toEpochMilli() < 5_000) {
                        future.cancel(true);
                    }
                });
            }
        }));
    }

    /**
     * Listens for Discord4J {@link Event}s and redistributes wrapped versions.
     *
     * @param event the Discord4J {@link Event} to listen for.
     */
    @EventSubscriber
    public static void handle(Event event) {
        Constructor<? extends BotEvent> constructor = EVENT_MAP.get(event.getClass());
        if (constructor != null) {
            try{EventDistributor.distribute(constructor.newInstance(event));
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                if (e.getCause() instanceof InvalidEventException) return;
                throw new RuntimeException("Improperly built BotEvent constructor", e);
            }
        }
    }

    public static void increaseParserPoolSize() {
        MESSAGE_PARSE_EXECUTOR.setCorePoolSize(MESSAGE_PARSE_EXECUTOR.getCorePoolSize() + 1);
        MESSAGE_PARSE_EXECUTOR.setMaximumPoolSize(MESSAGE_PARSE_EXECUTOR.getMaximumPoolSize() + 1);
    }
    public static void decreaseParserPoolSize() {
        MESSAGE_PARSE_EXECUTOR.setCorePoolSize(MESSAGE_PARSE_EXECUTOR.getCorePoolSize() - 1);
        MESSAGE_PARSE_EXECUTOR.setMaximumPoolSize(MESSAGE_PARSE_EXECUTOR.getMaximumPoolSize() - 1);
    }
}
