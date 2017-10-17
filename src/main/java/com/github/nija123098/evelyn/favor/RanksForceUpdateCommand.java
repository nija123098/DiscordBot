package com.github.nija123098.evelyn.favor;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.Configurable;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.Presence;
import com.github.nija123098.evelyn.discordobjects.wrappers.Role;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.favor.configs.EarnRankConfig;
import com.github.nija123098.evelyn.util.CallBuffer;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RanksForceUpdateCommand extends AbstractCommand  {
    private static final CallBuffer CALL_BUFFER = new CallBuffer(100);
    public RanksForceUpdateCommand() {
        super(RanksCommand.class, "forceupdate", null, null, null, "Forces all ranks on a server to update");
    }

    @Command
    public void command(Guild guild){
        Set<Role> earnedRanks = guild.getRoles().stream().filter(role -> ConfigHandler.getSetting(EarnRankConfig.class, role) != null).collect(Collectors.toSet());
        Set<User> users = guild.getUsers().stream().filter(user -> !user.isBot()).collect(Collectors.toSet());
        Set<User> priorityUsers = users.stream().filter(user -> user.getPresence().getStatus() == Presence.Status.ONLINE).filter(user -> {
            for (Role role : earnedRanks) if (user.getRolesForGuild(guild).contains(role)) return true;
            return false;
        }).collect(Collectors.toSet());
        users.removeAll(priorityUsers);
        Stream.concat(priorityUsers.stream(), users.stream()).map(user -> GuildUser.getGuildUser(guild, user)).forEach(guildUser -> CALL_BUFFER.call(() -> FavorChangeEvent.process(guildUser, () -> {})));
    }

    @Override
    public long getCoolDown(Class<? extends Configurable> clazz) {
        return clazz.equals(Guild.class) ? 604_800_000 : super.getCoolDown(clazz);// 7 days
    }
}
