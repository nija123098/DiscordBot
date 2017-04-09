package com.github.kaaz.emily.discordobjects;

import com.github.kaaz.emily.discordobjects.helpers.ReactionBehavior;
import com.github.kaaz.emily.discordobjects.wrappers.*;
import com.github.kaaz.emily.discordobjects.wrappers.event.BotEvent;
import com.github.kaaz.emily.discordobjects.wrappers.event.EventDistributor;
import com.github.kaaz.emily.discordobjects.wrappers.event.botevents.DiscordDataReload;
import com.github.kaaz.emily.programconfig.BotConfig;
import com.github.kaaz.emily.util.Log;
import org.reflections.Reflections;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.events.Event;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.GuildUpdateEvent;
import sx.blah.discord.handle.impl.events.guild.channel.ChannelUpdateEvent;
import sx.blah.discord.handle.impl.events.guild.role.RoleUpdateEvent;
import sx.blah.discord.handle.impl.events.user.UserUpdateEvent;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Made by nija123098 on 3/12/2017.
 */
public class DiscordAdapter {
    private static final Map<Class<? extends Event>, Constructor<? extends BotEvent>> EVENT_MAP;
    static {
        EVENT_MAP = new HashMap<>();
        new Reflections("com.github.kaaz.emily.discordobjects.wrappers.event.events").getSubTypesOf(BotEvent.class).forEach(clazz -> EVENT_MAP.put((Class<? extends Event>) clazz.getConstructors()[0].getParameterTypes()[0], (Constructor<? extends BotEvent>) clazz.getConstructors()[0]));
        ClientBuilder builder = new ClientBuilder();
        builder.withToken(BotConfig.BOT_TOKEN);
        builder.withRecommendedShardCount(true);
        builder.registerListener(DiscordAdapter.class);
        DiscordClient.set(builder.login());
        long targetTime = 20000 + 15000 * DiscordClient.getShardCount() + System.currentTimeMillis();
        while (true){
            try{Thread.sleep(1000);
            } catch (InterruptedException ignored) {}
            if (DiscordClient.isReady()){
                break;
            }
            if (targetTime < System.currentTimeMillis()){
                Log.log("Timeout while logging in");
                System.exit(-1);
            }
        }
        DiscordClient.online("with the loading screen!");
        EventDistributor.register(ReactionBehavior.class);
        EventDistributor.distribute(DiscordDataReload.class, () -> null);
    }

    /**
     * Forces the initialization of this class
     */
    public static void initialize(){
        Log.log("Discord adapter initialized");
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
    public static void handle(Event event){
        Constructor<? extends BotEvent> constructor = EVENT_MAP.get(event.getClass());
        if (constructor != null) {
            EventDistributor.distribute(constructor.getDeclaringClass(), () -> {
                try {
                    return constructor.newInstance(event);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException("Improperly built BotEvent constructor", e);
                }
            });
        }
    }
}
