package com.github.kaaz.emily.perms.commands;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ContextType;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Argument;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.Channel;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.Role;
import com.github.kaaz.emily.perms.configs.specialperms.GuildSpecialPermsConfig;
import com.github.kaaz.emily.perms.configs.specialperms.SpecialPermsContainer;

/**
 * Made by nija123098 on 6/13/2017.
 */
public class SpecialPermsModuleCommand extends AbstractCommand {
    public SpecialPermsModuleCommand() {
        super(SpecialPermsCommand.class, "module", null, null, null, "Manages special permissions for modules");
    }
    @Command
    public void command(Guild guild, MessageMaker maker, @Argument(optional = true, replacement = ContextType.NONE) Role role, @Argument(optional = true, replacement = ContextType.NONE) Channel channel, @Argument Boolean allow, @Argument ModuleLevel level){
        SpecialPermsContainer container = ConfigHandler.getSetting(GuildSpecialPermsConfig.class, guild);
        if (container == null) {
            maker.append("You must enable special perms using @Emily commandadmin enable");
            return;
        }
        container.addModule(allow, channel, role, level);
    }
}
