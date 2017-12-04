package com.github.nija123098.evelyn.perms.commands;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.Role;
import com.github.nija123098.evelyn.perms.configs.specialperms.GuildSpecialPermsConfig;
import com.github.nija123098.evelyn.perms.configs.specialperms.SpecialPermsContainer;

import static com.github.nija123098.evelyn.command.ContextType.NONE;
import static com.github.nija123098.evelyn.config.ConfigHandler.getSetting;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class SpecialPermsCommandCommand extends AbstractCommand {
    public SpecialPermsCommandCommand() {
        super(SpecialPermsCommand.class, "command", null, null, null, "configures the special command permissions");
    }

    @Command
    public void command(Guild guild, MessageMaker maker, @Argument(optional = true, replacement = NONE) Role role, @Argument(optional = true, replacement = NONE) Channel channel, @Argument Boolean allow, @Argument AbstractCommand command) {
        SpecialPermsContainer container = getSetting(GuildSpecialPermsConfig.class, guild);
        if (container == null) {
            maker.append("You must enable special perms using @Evelyn commandadmin enable");
            return;
        }
        container.addCommand(allow, channel, role, command);
    }
}
