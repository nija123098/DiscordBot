package com.github.nija123098.evelyn.perms.commands;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ContextType;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.Role;
import com.github.nija123098.evelyn.perms.configs.specialperms.GuildSpecialPermsConfig;
import com.github.nija123098.evelyn.perms.configs.specialperms.SpecialPermsContainer;

/**
 * Made by nija123098 on 6/13/2017.
 */
public class SpecialPermsCommandCommand extends AbstractCommand {
    public SpecialPermsCommandCommand() {
        super(SpecialPermsCommand.class, "command", null, null, null, "configures the special command permissions");
    }
    @Command
    public void command(Guild guild, MessageMaker maker, @Argument(optional = true, replacement = ContextType.NONE) Role role, @Argument(optional = true, replacement = ContextType.NONE) Channel channel, @Argument Boolean allow, @Argument AbstractCommand command){
        SpecialPermsContainer container = ConfigHandler.getSetting(GuildSpecialPermsConfig.class, guild);
        if (container == null) {
            maker.append("You must enable special perms using @Evelyn commandadmin enable");
            return;
        }
        container.addCommand(allow, channel, role, command);
    }
}
