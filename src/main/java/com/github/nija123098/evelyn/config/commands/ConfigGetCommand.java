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
import com.github.nija123098.evelyn.exception.ArgumentException;
import com.github.nija123098.evelyn.tag.Tag;
import com.github.nija123098.evelyn.tag.Tags;
import com.github.nija123098.evelyn.util.FormatHelper;

import java.awt.*;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class ConfigGetCommand extends AbstractCommand {
    public ConfigGetCommand() {
        super(ConfigCommand.class, "get", "get", null, null, "Gets the value of a config for a configurable");
    }
    @Command
    @ConfigurableTypeAddLocation("The array must have a additional index, ordered by ordinal in ConfigLevel")
    public <T extends Configurable> void command(@Argument AbstractConfig<?, T> config, @Argument(optional = true) T target, MessageMaker maker, @Context(softFail = true) Track track, @Context(softFail = true) Playlist playlist, User user, Channel channel, @Context(softFail = true) GuildUser guildUser, @Context(softFail = true) Guild guild) {
        target = (T) new Configurable[]{track, playlist, user, channel, channel.getCategory(), guildUser, target instanceof Role ? target : null, guild, GlobalConfigurable.GLOBAL, target != null ? target : guild == null ? user : guild}[config.getConfigLevel().ordinal()];
        if (target == null) throw new ArgumentException("No context for " + config.getConfigLevel() + " was able to be gotten, check your spelling");
        else if (config.getConfigLevel() != ConfigLevel.ALL) target = (T) target.convert(config.getConfigLevel().getType());
        maker.mustEmbed();
        maker.appendRaw(FormatHelper.embedLink(config.getDisplayName(), "") + " is set to " + config.getExteriorValue(target));// morph exception should throw before cast exception
    }
}
