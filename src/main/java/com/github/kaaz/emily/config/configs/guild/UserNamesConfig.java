package com.github.kaaz.emily.config.configs.guild;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.discordobjects.wrappers.DiscordClient;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.discordobjects.wrappers.event.EventListener;
import com.github.kaaz.emily.discordobjects.wrappers.event.botevents.DiscordDataReload;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordNicknameChange;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordUserJoin;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordUserLeave;
import com.github.kaaz.emily.perms.BotRole;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Made by nija123098 on 3/31/2017.
 */
public class UserNamesConfig extends AbstractConfig<Set<String>, Guild> {
    public UserNamesConfig() {
        super("user_names", BotRole.BOT_OWNER, new HashSet<>(),
                "The list of user names and nicknames for that guild for optimization which is automatically managed");
    }
    @EventListener
    public void handle(DiscordUserJoin join){
        Set<String> set = ConfigHandler.getSetting(UserNamesConfig.class, join.getGuild());
        set.add(join.getUser().getName());
        ConfigHandler.setSetting(UserNamesConfig.class, join.getGuild(), set);
    }
    @EventListener
    public void handle(DiscordUserLeave leave){
        Set<String> set = ConfigHandler.getSetting(UserNamesConfig.class, leave.getGuild());
        set.remove(leave.getUser().getName());
        ConfigHandler.setSetting(UserNamesConfig.class, leave.getGuild(), set);
    }
    @EventListener
    public void handler(DiscordNicknameChange change){
        Set<String> set = ConfigHandler.getSetting(UserNamesConfig.class, change.getGuild());
        if (change.getNewUsername() != null){
            set.add(change.getNewUsername());
        }
        if (change.getOldUsername() != null){
            set.remove(change.getOldUsername());
        }
        ConfigHandler.setSetting(UserNamesConfig.class, change.getGuild(), set);
    }
    @EventListener
    public void handle(DiscordDataReload reload){
        DiscordClient.getGuilds().forEach(guild -> ConfigHandler.setSetting(UserNamesConfig.class, guild, guild.getUsers().stream().map(User::getName).collect(Collectors.toSet())));
    }
}
