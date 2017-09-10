package com.github.nija123098.evelyn.config.commands;

import com.github.nija123098.evelyn.audio.Playlist;
import com.github.nija123098.evelyn.audio.Track;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Context;
import com.github.nija123098.evelyn.config.ConfigLevel;
import com.github.nija123098.evelyn.config.Configurable;
import com.github.nija123098.evelyn.config.GlobalConfigurable;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.Role;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

/**
 * Made by nija123098 on 6/2/2017.
 */
public class TargetHelper {
    private static <T extends Configurable> Configurable getConfigurable(ConfigLevel level, @Argument(optional = true) T target, String arg, @Context(softFail = true) Track track, @Context(softFail = true) Playlist playlist, User user, Channel channel, @Context(softFail = true) GuildUser guildUser, @Context(softFail = true) Guild guild){
        return new Configurable[]{track, playlist, user, channel, guildUser, target instanceof Role ? target : null, guild, GlobalConfigurable.GLOBAL, target}[level.ordinal()].convert(level.getType());
    }
}
