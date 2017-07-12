package com.github.kaaz.emily.command;

import com.github.kaaz.emily.discordobjects.wrappers.DiscordClient;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.event.EventListener;
import com.github.kaaz.emily.discordobjects.wrappers.event.botevents.DiscordDataReload;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordGuildJoin;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordNicknameChange;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordUserJoin;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordUserLeave;
import org.eclipse.jetty.util.ConcurrentHashSet;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Made by nija123098 on 3/31/2017.
 */
public class UserNameMonitor {
    private static final Map<Guild, Set<String>> MAP = new ConcurrentHashMap<>();
    @EventListener
    public static void handle(DiscordGuildJoin join){
        loadGuild(join.getGuild());
    }
    @EventListener
    public static void handle(DiscordUserJoin join){
        Set<String> set = MAP.computeIfAbsent(join.getGuild(), guild -> new ConcurrentHashSet<>());
        set.add(join.getUser().getName());
    }
    @EventListener
    public static void handle(DiscordUserLeave leave){
        Set<String> set = MAP.computeIfAbsent(leave.getGuild(), guild -> new ConcurrentHashSet<>());
        set.remove(leave.getUser().getName());
        String nick = leave.getUser().getNickname(leave.getGuild());
        if (nick != null) set.remove(nick);
    }
    @EventListener
    public static void handler(DiscordNicknameChange change){
        Set<String> set = MAP.computeIfAbsent(change.getGuild(), guild -> new ConcurrentHashSet<>());
        if (change.getNewUsername() != null) set.add(change.getNewUsername());
        if (change.getOldUsername() != null) set.remove(change.getOldUsername());
    }
    @EventListener
    public static void handle(DiscordDataReload reload){
        DiscordClient.getGuilds().forEach(UserNameMonitor::loadGuild);
    }
    private static void loadGuild(Guild guild){
        Set<String> strings = new ConcurrentHashSet<>();
        guild.getUsers().forEach(user -> {
            String name = user.getName();
            strings.add(name);
            String display = user.getDisplayName(guild);
            if (!display.equals(name)) strings.add(display);
        });
        MAP.put(guild, strings);
    }
    public static Set<String> getNames(Guild guild) {
        return MAP.get(guild);
    }
}
