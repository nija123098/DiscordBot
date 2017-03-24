package com.github.kaaz.emily.discordobjects;

import com.github.kaaz.emily.programconfig.BotConfig;
import com.github.kaaz.emily.discordobjects.wrappers.*;
import com.github.kaaz.emily.discordobjects.wrappers.event.DiscordEvent;
import com.github.kaaz.emily.discordobjects.wrappers.event.EventDistributor;
import com.github.kaaz.emily.discordobjects.wrappers.event.EventListener;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.UserRolesUpdate;
import org.reflections.Reflections;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.events.Event;
import sx.blah.discord.handle.impl.events.guild.GuildUpdateEvent;
import sx.blah.discord.handle.impl.events.guild.channel.ChannelUpdateEvent;
import sx.blah.discord.handle.impl.events.guild.member.UserRoleUpdateEvent;
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
    private static final Map<Class<Event>, Constructor<DiscordEvent>> EVENT_MAP;
    static {
        ClientBuilder builder = new ClientBuilder();
        builder.withToken(BotConfig.BOT_TOKEN);
        builder.withRecommendedShardCount(true);
        builder.registerListener(DiscordAdapter.class);
        DiscordClient.set(builder.login());
        EVENT_MAP = new HashMap<>();
        Reflections reflections = new Reflections("com.github.kaaz.emily.discordobjects.wrappers.event.events");
        reflections.getSubTypesOf(DiscordEvent.class).forEach(clazz -> EVENT_MAP.put((Class<Event>) clazz.getConstructors()[0].getParameterTypes()[0], (Constructor<DiscordEvent>) clazz.getConstructors()[0]));
    }
    @EventListener
    public static void handle(UserUpdateEvent event){
        User.update(event.getNewUser());
    }
    @EventListener
    public static void handle(GuildUpdateEvent event){
        Guild.update(event.getNewGuild());
    }
    @EventListener
    public static void handle(RoleUpdateEvent event){
        Role.update(event.getNewRole());
    }
    @EventListener
    public static void handle(ChannelUpdateEvent event){
        Channel.update(event.getNewChannel());
    }
    @EventListener
    public static void handle(UserRoleUpdateEvent event){
        EventDistributor.distribute(UserRolesUpdate.class, () -> new UserRolesUpdate(event));
    }
    @EventListener
    public static void handle(Event event){
        Constructor<DiscordEvent> constructor = EVENT_MAP.get(event);
        if (constructor != null) {
            EventDistributor.distribute(event.getClass(), () -> {
                try {
                    return constructor.newInstance(event);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException("Improperly built DiscordEvent constructor", e);
                }
            });
        }
    }
}
