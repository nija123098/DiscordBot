package com.github.nija123098.evelyn.command;

import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordGuildJoin;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordNicknameChange;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordUserJoin;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordUserLeave;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Assists in keeping an active list of
 * all names any user goes by in a guild.
 *
 * @author nija123098
 * @since 1.0.0
 */
public class UserNameMonitor {
    private static final Map<Guild, Set<String>> MAP = new ConcurrentHashMap<>();
    static {
        DiscordClient.getGuilds().forEach(UserNameMonitor::loadGuild);
    }
    @EventListener
    public static void handle(DiscordGuildJoin join){
        loadGuild(join.getGuild());
    }
    @EventListener
    public static void handle(DiscordUserJoin join){
        Set<String> set = MAP.computeIfAbsent(join.getGuild(), guild -> ConcurrentHashMap.newKeySet());
        set.add(join.getUser().getName());
    }
    @EventListener
    public static void handle(DiscordUserLeave leave){
        Set<String> set = MAP.computeIfAbsent(leave.getGuild(), guild -> ConcurrentHashMap.newKeySet());
        set.remove(leave.getUser().getName());
        String nick = leave.getUser().getNickname(leave.getGuild());
        if (nick != null) set.remove(nick);
    }
    @EventListener
    public static void handler(DiscordNicknameChange change){
        Set<String> set = MAP.computeIfAbsent(change.getGuild(), guild -> ConcurrentHashMap.newKeySet());
        if (change.getNewUsername() != null) set.add(change.getNewUsername());
        if (change.getOldUsername() != null) set.remove(change.getOldUsername());
    }
    private static void loadGuild(Guild guild){
        Set<String> strings = ConcurrentHashMap.newKeySet();
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
