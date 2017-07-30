package com.github.kaaz.emily.discordobjects;

import com.github.kaaz.emily.audio.SpeechParser;
import com.github.kaaz.emily.automoderation.messagefiltering.MessageMonitor;
import com.github.kaaz.emily.command.CommandHandler;
import com.github.kaaz.emily.discordobjects.helpers.ReactionBehavior;
import com.github.kaaz.emily.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.github.kaaz.emily.discordobjects.wrappers.*;
import com.github.kaaz.emily.discordobjects.wrappers.event.BotEvent;
import com.github.kaaz.emily.discordobjects.wrappers.event.EventDistributor;
import com.github.kaaz.emily.discordobjects.wrappers.event.botevents.DiscordDataReload;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordMessageReceived;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordVoiceJoin;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordVoiceLeave;
import com.github.kaaz.emily.launcher.BotConfig;
import com.github.kaaz.emily.launcher.Launcher;
import com.github.kaaz.emily.launcher.Reference;
import com.github.kaaz.emily.service.services.MemoryManagementService;
import com.github.kaaz.emily.service.services.ScheduleService;
import com.github.kaaz.emily.template.KeyPhrase;
import com.github.kaaz.emily.template.Template;
import com.github.kaaz.emily.template.TemplateHandler;
import com.github.kaaz.emily.util.Log;
import com.github.kaaz.emily.util.ThreadProvider;
import org.reflections.Reflections;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.events.Event;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.GuildUpdateEvent;
import sx.blah.discord.handle.impl.events.guild.channel.ChannelUpdateEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.guild.role.RoleUpdateEvent;
import sx.blah.discord.handle.impl.events.guild.voice.user.UserVoiceChannelMoveEvent;
import sx.blah.discord.handle.impl.events.shard.ShardReadyEvent;
import sx.blah.discord.handle.impl.events.user.UserUpdateEvent;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Made by nija123098 on 3/12/2017.
 */
public class DiscordAdapter {
    private static final Map<Class<? extends Event>, Constructor<? extends BotEvent>> EVENT_MAP = new HashMap<>();
    private static final long PLAY_TEXT_SPEED = 60_000;
    private static final List<Template> PREVIOUS_TEXTS = new MemoryManagementService.ManagedList<>(PLAY_TEXT_SPEED + 1000);// a second for execution time
    static {
        new Reflections(Reference.BASE_PACKAGE + ".discordobjects.wrappers.event.events").getSubTypesOf(BotEvent.class).stream().filter(clazz -> !clazz.equals(DiscordMessageReceived.class)).forEach(clazz -> EVENT_MAP.put((Class<? extends Event>) clazz.getConstructors()[0].getParameterTypes()[0], (Constructor<? extends BotEvent>) clazz.getConstructors()[0]));
        ClientBuilder builder = new ClientBuilder();
        builder.withToken(BotConfig.BOT_TOKEN);
        builder.withRecommendedShardCount();
        builder.withMaximumDispatchThreads(64);
        DiscordClient.set(builder.login());
        try{DiscordClient.client().getDispatcher().waitFor(ReadyEvent.class, 20 + 25 * DiscordClient.getShardCount(), TimeUnit.MINUTES);
            Thread.sleep(10_000);
        } catch (InterruptedException e) {
            Log.log("Could not launch in time", e);
        }
        GuildAudioManager.init();
        SpeechParser.init();
        DiscordClient.client().getDispatcher().registerListener(ThreadProvider.getExecutorService(), EventDistributor.class);
        DiscordClient.client().getDispatcher().registerListener(ThreadProvider.getExecutorService(), DiscordAdapter.class);
        EventDistributor.register(ReactionBehavior.class);
        EventDistributor.register(MessageMonitor.class);
        EventDistributor.distribute(DiscordDataReload.class, null);
        if (!BotConfig.GHOST_MODE) ScheduleService.scheduleRepeat(PLAY_TEXT_SPEED + 10_000, PLAY_TEXT_SPEED, () -> {
            Template template = TemplateHandler.getTemplate(KeyPhrase.PLAY_TEXT, null, PREVIOUS_TEXTS);
            if (template != null) DiscordClient.getShards().forEach(shard -> shard.online(template.interpret((User) null, shard, null, null, null, null)));
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
    public static void handle(UserVoiceChannelMoveEvent event){
        EventDistributor.distribute(new DiscordVoiceLeave(event.getOldChannel(), event.getUser()));
        EventDistributor.distribute(new DiscordVoiceJoin(event.getNewChannel(), event.getUser()));
    }
    @EventSubscriber
    public static void handle(MessageReceivedEvent event){
        if (event.getAuthor().isBot() || !Launcher.isReady() || event.getMessage().getContent() == null || event.getMessage().getContent().isEmpty()) return;
        DiscordMessageReceived receivedEvent = new DiscordMessageReceived((sx.blah.discord.handle.impl.events.MessageReceivedEvent) event);
        if (MessageMonitor.monitor(receivedEvent)) return;
        receivedEvent.setCommand(CommandHandler.handle(receivedEvent));
        EventDistributor.distribute(receivedEvent);
    }
    @EventSubscriber
    public static void handle(Event event){
        Constructor<? extends BotEvent> constructor = EVENT_MAP.get(event.getClass());// check this won't be duplicating messages received
        if (constructor != null) {
            try{EventDistributor.distribute(constructor.newInstance(event));
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException("Improperly built BotEvent constructor", e);
            }
        }
    }
}
