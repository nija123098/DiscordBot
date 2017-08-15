package com.github.kaaz.emily.template.commands.customcommand;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.exeption.ArgumentException;
import com.github.kaaz.emily.template.CustomCommand;
import com.github.kaaz.emily.template.CustomCommandConfig;

/**
 * Made by nija123098 on 8/13/2017.
 */
public class CustomCommandRagexCommand extends AbstractCommand {
    public CustomCommandRagexCommand() {
        super(CustomCommandCommand.class, "ragex", null, null, null, "Adds a ragex search to activate a command on text without a prefix");
    }
    @Command
    public void command(Guild guild, String s){
        int space = s.indexOf(" ");
        if (space == -1) throw new ArgumentException("Please use the following syntax: <name> <ragex>");
        String name = s.substring(0, space).toLowerCase();
        String ragex = s.substring(space).trim();
        ConfigHandler.alterSetting(CustomCommandConfig.class, guild, commands -> {
            for (CustomCommand command : commands){
                if (command.getName().equals(name)) {
                    command.setRagex(ragex);
                    return;
                }
            }
            throw new ArgumentException("There was no custom command by that name");
        });
    }
}
