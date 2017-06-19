package com.github.kaaz.emily.config.commands;

import com.github.kaaz.emily.audio.Playlist;
import com.github.kaaz.emily.audio.Track;
import com.github.kaaz.emily.command.anotations.Argument;
import com.github.kaaz.emily.command.anotations.Context;
import com.github.kaaz.emily.config.*;
import com.github.kaaz.emily.discordobjects.wrappers.*;

/**
 * Made by nija123098 on 6/2/2017.
 */
public class TargetHelper {
    private static <T extends Configurable> Configurable getConfigurable(ConfigLevel level, @Argument(optional = true) T target, String arg, @Context(softFail = true) Track track, @Context(softFail = true) Playlist playlist, User user, Channel channel, @Context(softFail = true) GuildUser guildUser, @Context(softFail = true) Guild guild){
        return new Configurable[]{track, playlist, user, channel, guildUser, target instanceof Role ? target : null, guild, GlobalConfigurable.GLOBAL, target}[level.ordinal()].convert(level.getType());
    }
}
