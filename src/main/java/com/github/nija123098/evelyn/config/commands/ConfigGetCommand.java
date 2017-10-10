package com.github.nija123098.evelyn.config.commands;

import com.github.nija123098.evelyn.audio.Playlist;
import com.github.nija123098.evelyn.audio.Track;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.command.annotations.Context;
import com.github.nija123098.evelyn.config.*;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.Role;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

/**
 * Made by nija123098 on 4/2/2017.
 */
public class ConfigGetCommand extends AbstractCommand {
    public ConfigGetCommand() {
        super(ConfigCommand.class, "get", null, null, null, "Gets the value of a config for a configurable");
    }
    @Command
    @ConfigurableTypeAddLocation("The array must have a additional index, ordered by ordinal in ConfigLevel")
    public <T extends Configurable> void command(@Argument AbstractConfig<?, T> config, @Argument(optional = true) T target, MessageMaker maker, @Context(softFail = true) Track track, @Context(softFail = true) Playlist playlist, User user, Channel channel, @Context(softFail = true) GuildUser guildUser, @Context(softFail = true) Guild guild){
        target = (T) new Configurable[]{track, playlist, user, channel, channel.getCategory(), guildUser, target instanceof Role ? target : null, guild, GlobalConfigurable.GLOBAL, target}[config.getConfigLevel().ordinal()].convert(config.getConfigLevel().getType());
        maker.appendRaw(config.getExteriorValue(target));// morph exception should throw before cast exception
    }
}
