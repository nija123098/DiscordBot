package com.github.kaaz.emily.config.commands;

import com.github.kaaz.emily.audio.Playlist;
import com.github.kaaz.emily.audio.Track;
import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.anotations.Argument;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.command.anotations.Context;
import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.config.Configurable;
import com.github.kaaz.emily.config.GlobalConfigurable;
import com.github.kaaz.emily.config.GuildUser;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.*;

/**
 * Made by nija123098 on 4/2/2017.
 */
public class ConfigGetCommand extends AbstractCommand {
    public ConfigGetCommand() {
        super(ConfigCommand.class, "get", null, null, null, "Gets the value of a config for a configurable");
    }
    @Command
    public <T extends Configurable> void command(@Argument AbstractConfig<?, T> config, @Argument(optional = true) T target, MessageMaker maker, @Context(softFail = true) Track track, @Context(softFail = true) Playlist playlist, User user, Channel channel, @Context(softFail = true) GuildUser guildUser, @Context(softFail = true) Guild guild){
        target = (T) new Configurable[]{track, playlist, user, channel, guildUser, target instanceof Role ? target : null, guild, GlobalConfigurable.GLOBAL, target}[config.getConfigLevel().ordinal()].convert(config.getConfigLevel().getType());
        maker.appendRaw(config.getExteriorValue(target));// morph exception should throw before cast exception
    }
}
