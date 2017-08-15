package com.github.kaaz.emily.template.commands.customcommand;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.exeption.ArgumentException;
import com.github.kaaz.emily.template.CustomCommand;
import com.github.kaaz.emily.template.CustomCommandConfig;
import com.github.kaaz.emily.template.CustomCommandHandler;

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
