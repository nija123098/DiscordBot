package com.github.kaaz.emily.config.commands;

import com.github.kaaz.emily.audio.Playlist;
import com.github.kaaz.emily.audio.Track;
import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.anotations.Argument;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.command.anotations.Context;
import com.github.kaaz.emily.config.*;
import com.github.kaaz.emily.discordobjects.wrappers.*;
import com.github.kaaz.emily.exeption.PermissionsException;

/**
 * Made by nija123098 on 4/2/2017.
 */
public class ConfigSetCommand extends AbstractCommand {
    public ConfigSetCommand() {
        super(ConfigCommand.class, "set", null, null, null, "Sets");
    }
    @Command
    public <T extends Configurable> void command(@Argument AbstractConfig<?, T> config, @Argument(optional = true) T target, String arg, @Context(softFail = true) Track track, @Context(softFail = true) Playlist playlist, User user, Channel channel, @Context(softFail = true) GuildUser guildUser, @Context(softFail = true) Guild guild){
        try {
            target = (T) new Configurable[]{track, playlist, user, channel, guildUser, target instanceof Role ? target : null, guild, GlobalConfigurable.GLOBAL, target}[config.getConfigLevel().ordinal()].convert(config.getConfigLevel().getType());
            target.checkPermissionToEdit(user, guild);// morph exception should throw before cast exception
            if (!config.requiredBotRole().hasRequiredRole(user, guild)){
                throw new PermissionsException("You must be at least a " + config.requiredBotRole().name() + " to edit that config");
            }
            ConfigHandler.setExteriorSetting(config.getClass(), target, arg);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
