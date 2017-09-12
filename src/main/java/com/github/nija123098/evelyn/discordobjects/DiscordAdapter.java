package com.github.nija123098.evelyn.discordobjects;

import com.github.nija123098.evelyn.audio.SpeechParser;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.*;
import com.github.nija123098.evelyn.moderation.DeletePinNotificationConfig;
import com.github.nija123098.evelyn.moderation.messagefiltering.MessageMonitor;
import com.github.nija123098.evelyn.chatbot.ChatBot;
import com.github.nija123098.evelyn.command.CommandHandler;
import com.github.nija123098.evelyn.discordobjects.exception.ErrorWrapper;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.helpers.ReactionBehavior;
import com.github.nija123098.evelyn.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.github.nija123098.evelyn.discordobjects.wrappers.*;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.BotEvent;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventDistributor;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.botevents.DiscordDataReload;
import com.github.nija123098.evelyn.launcher.BotConfig;
import com.github.nija123098.evelyn.launcher.Launcher;
import com.github.nija123098.evelyn.launcher.Reference;
import com.github.nija123098.evelyn.perms.ContributorMonitor;
import com.github.nija123098.evelyn.service.services.MemoryManagementService;
import com.github.nija123098.evelyn.service.services.ScheduleService;
import com.github.nija123098.evelyn.template.KeyPhrase;
import com.github.nija123098.evelyn.template.Template;
import com.github.nija123098.evelyn.template.TemplateHandler;
import com.github.nija123098.evelyn.util.Care;
import com.github.nija123098.evelyn.util.EmoticonHelper;
import com.github.nija123098.evelyn.util.Log;
import com.github.nija123098.evelyn.util.ThreadProvider;
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
import sx.blah.discord.handle.impl.events.guild.channel.ChannelUpdateEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MentionEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageUpdateEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionEvent;
import sx.blah.discord.handle.impl.events.guild.member.UserBanEvent;
import sx.blah.discord.handle.impl.events.guild.member.UserLeaveEvent;
import sx.blah.discord.handle.impl.events.guild.role.RoleUpdateEvent;
import sx.blah.discord.handle.impl.events.guild.voice.user.UserVoiceChannelMoveEvent;
import sx.blah.discord.handle.impl.events.shard.ShardReadyEvent;
import sx.blah.discord.handle.impl.events.user.UserUpdateEvent;
import sx.blah.discord.handle.impl.obj.ReactionEmoji;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.Permissions;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Made by nija123098 on 3/12/2017.
 */
public class DiscordAdapter {
    private static final Map<Class<? extends Event>, Constructor<? extends BotEvent>> EVENT_MAP;
    private static final long PLAY_TEXT_SPEED = 60_000;
    private static final List<Template> PREVIOUS_TEXTS = new MemoryManagementService.ManagedList<>(PLAY_TEXT_SPEED + 1000);// a second for execution time
    private static final AtomicBoolean BOT_LAG_LOCKED = new AtomicBoolean();
    public static final ScheduleService.ScheduledRepeatedTask PLAY_TEXT_UPDATER;
    static {
        Set<Class<? extends BotEvent>> classes = new Reflections(Reference.BASE_PACKAGE + ".discordobjects.wrappers.event.events").getSubTypesOf(BotEvent.class);
        classes.remove(DiscordMessageReceived.class);
        EVENT_MAP = new HashMap<>(classes.size() + 2, 1);
        classes.stream().filter(clazz -> !clazz.equals(DiscordMessageReceived.class)).filter(clazz -> !clazz.equals(DiscordUserLeave.class)).filter(clazz -> !clazz.isAssignableFrom(ReactionEvent.class)).map(clazz -> clazz.getConstructors()[0]).forEach(constructor -> EVENT_MAP.put((Class<? extends Event>) constructor.getParameterTypes()[0], (Constructor<? extends BotEvent>) constructor));
        ClientBuilder builder = new ClientBuilder();
        builder.withToken(BotConfig.BOT_TOKEN);
        builder.setMaxMessageCacheCount(30);
        builder.withMaximumDispatchThreads(2);
        builder.registerListener((IListener<ShardReadyEvent>) event -> event.getShard().idle("with the login screen!"));
        int total = Requests.GENERAL_REQUESTS.GET.makeRequest(DiscordEndpoints.GATEWAY + "/bot", GatewayBotResponse.class, new BasicNameValuePair("Authorization", "Bot " + BotConfig.BOT_TOKEN), new BasicNameValuePair("Content-Type", "application/json")).shards;
        List<Integer> list = new ArrayList<>(total);
        for (int i = 0; i < total; i++) if (i % BotConfig.TOTAL_EMILYS == BotConfig.EMILY_NUMBER) list.add(i);
        DiscordClient.set(list.stream().map(integer -> builder.setShard(integer, total)).map(ClientBuilder::login).collect(Collectors.toList()));
        int i = 20 + 25 * DiscordClient.getShardCount();
        for (; i > -1; --i) {
            if (DiscordClient.isReady()) break;
            Care.lessSleep(1000);
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
            DiscordClient.clients().forEach(client -> client.getDispatcher().registerListener(ThreadProvider.getExecutorService(), EventDistributor.class));
            DiscordClient.clients().forEach(client -> client.getDispatcher().registerListener(ThreadProvider.getExecutorService(), DiscordAdapter.class));
            EventDistributor.register(ReactionBehavior.class);
            EventDistributor.register(MessageMonitor.class);
            EventDistributor.distribute(DiscordDataReload.class, null);
        });
        if (!BotConfig.GHOST_MODE) ScheduleService.scheduleRepeat(PLAY_TEXT_SPEED + 10_000, PLAY_TEXT_SPEED, () -> {
            Template template = TemplateHandler.getTemplate(KeyPhrase.PLAY_TEXT, null, PREVIOUS_TEXTS);
            if (template != null) DiscordClient.getShards().forEach(shard -> shard.online(template.interpret((User) null, shard, null, null, null, null)));
        });
        AtomicInteger count = new AtomicInteger();
        PLAY_TEXT_UPDATER = ScheduleService.scheduleRepeat(5000, 5000, () -> {
            if (!DiscordClient.isReady()) return;
            AtomicLong responseTime = new AtomicLong();
            DiscordClient.getShards().forEach(shard -> responseTime.addAndGet(shard.getResponseTime()));
            boolean result = responseTime.get() / DiscordClient.getShardCount() > 2500;
            if (result != BOT_LAG_LOCKED.get()){
                if (count.incrementAndGet() >= 4){
                    BOT_LAG_LOCKED.set(result);
                    Log.log("Now " + (result ? "" : "un") + "locking bot due to lag - " + (responseTime.get() / DiscordClient.getShardCount()));
                }
            }else count.set(0);
        });
        Path path = Paths.get(BotConfig.STATS_OVER_TIME);
        File file = path.toFile();
        if (!file.exists()) {
            try{file.createNewFile();
            } catch (IOException e) {
                Log.log("Could not make a file for tracking guild count", e);
            }
        }
        ScheduleService.scheduleRepeat(System.currentTimeMillis() % 86_400_000, 86_400_000, () -> {// 24 hours
            long time = System.currentTimeMillis();
            time -= time % 86_400_000;
            while (!DiscordClient.isReady()) Care.lessSleep(1000);
            try{Files.write(path, Collections.singleton(time + " " + DiscordClient.getGuilds().size()), StandardOpenOption.APPEND);
            } catch (IOException e) {
                Log.log("Could not save guild count", e);
            }
        });
    }
    /**
     * Forces the initialization of this class
     */
    public static void initialize(){
        Log.log("Discord adapter initialized");
    }
    @EventSubscriber
    public static void handle(ShardReadyEvent event){
        event.getShard().idle("with the loading screen!");
    }
    @EventSubscriber
    public static void handle(UserUpdateEvent event){
        User.update(event.getNewUser());
    }
    @EventSubscriber
    public static void handle(GuildUpdateEvent event){
        Guild.update(event.getNewGuild());
    }
    @EventSubscriber
    public static void handle(RoleUpdateEvent event){
        Role.update(event.getNewRole());
    }
    @EventSubscriber
    public static void handle(ChannelUpdateEvent event){
        Channel.update(event.getNewChannel());
    }
    @EventSubscriber
    public static void handle(MessageUpdateEvent event){
        Message.update(event.getNewMessage());
    }
    @EventSubscriber
    public static void handle(UserVoiceChannelMoveEvent event){
        EventDistributor.distribute(new DiscordVoiceLeave(event.getOldChannel(), event.getUser()));
        EventDistributor.distribute(new DiscordVoiceJoin(event.getNewChannel(), event.getUser()));
    }
    @EventSubscriber
    public static void handle(ReactionEvent event){// it's cleaner than the alternative
        EventDistributor.distribute(new DiscordReactionEvent(event));
    }
    private static final List<IMessage> MENTIONED_MESSAGES = new MemoryManagementService.ManagedList<>(2_000);
    @EventSubscriber
    public static void handle(MentionEvent event){
        ScheduleService.schedule(5 * event.getMessage().getShard().getResponseTime(), () -> {
            if (!event.getChannel().getModifiedPermissions(DiscordClient.getOurUser().user()).contains(Permissions.ADD_REACTIONS)) return;
            if (MENTIONED_MESSAGES.contains(event.getMessage())) ErrorWrapper.wrap(() -> event.getMessage().addReaction(ReactionEmoji.of(EmoticonHelper.getChars("eyes", false))));
        });
    }
    @EventSubscriber
    public static void handle(MessageReceivedEvent event){
        if (event.getAuthor().isBot() || !Launcher.isReady() || event.getMessage().getContent() == null) return;
        if (event.getMessage().getContent().isEmpty()) DeletePinNotificationConfig.handle(new DiscordMessageReceived((sx.blah.discord.handle.impl.events.MessageReceivedEvent) event));
        DiscordMessageReceived receivedEvent = new DiscordMessageReceived((sx.blah.discord.handle.impl.events.MessageReceivedEvent) event);
        if (MessageMonitor.monitor(receivedEvent)) return;
        Boolean parseForCommand = CommandHandler.handle(receivedEvent);
        if (parseForCommand == null){
            String thought = receivedEvent.getMessage().getContent();
            if (ChatBot.mayChat(receivedEvent.getChannel(), receivedEvent.getMessage().getContent())) {
                new MessageMaker(receivedEvent.getChannel()).appendRaw(ChatBot.getChatBot(receivedEvent.getChannel()).think(thought)).send();
            }
            receivedEvent.setCommand(true);
        } else {
            receivedEvent.setCommand(parseForCommand);
            if (!parseForCommand) MENTIONED_MESSAGES.add(receivedEvent.getMessage().message());
        }
        EventDistributor.distribute(receivedEvent);
    }
    @EventSubscriber
    public static void handle(Event event){
        if (BOT_LAG_LOCKED.get()) return;
        Constructor<? extends BotEvent> constructor = EVENT_MAP.get(event.getClass());
        if (constructor != null) {
            try{EventDistributor.distribute(constructor.newInstance(event));
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException("Improperly built BotEvent constructor", e);
            }
        }
    }
}
