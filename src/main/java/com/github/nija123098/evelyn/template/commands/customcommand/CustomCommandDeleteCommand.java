package com.github.nija123098.evelyn.template.commands.customcommand;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.exeption.ArgumentException;
import com.github.nija123098.evelyn.template.CustomCommand;
import com.github.nija123098.evelyn.template.CustomCommandConfig;
import com.github.nija123098.evelyn.template.CustomCommandHandler;

/**
 * Made by nija123098 on 8/11/2017.
 */
public class CustomCommandDeleteCommand extends AbstractCommand {
    public CustomCommandDeleteCommand() {
        super(CustomCommandCommand.class, "delete", null, null, "remove", "Deletes a command by name");
    }
    @Command
    public void command(String s, Guild guild){
        ConfigHandler.alterSetting(CustomCommandConfig.class, guild, customCommands -> {
            for (CustomCommand command : customCommands){
                if (command.getName().equals(s)){
                    customCommands.remove(command);
                    return;
                }
            }
            throw new ArgumentException("That command doesn't exist");
        });
        CustomCommandHandler.loadGuild(guild);
    }
}
