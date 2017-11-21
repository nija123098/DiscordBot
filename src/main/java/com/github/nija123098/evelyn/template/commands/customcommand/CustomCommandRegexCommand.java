package com.github.nija123098.evelyn.template.commands.customcommand;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.exception.ArgumentException;
import com.github.nija123098.evelyn.template.CustomCommand;
import com.github.nija123098.evelyn.template.CustomCommandConfig;

/**
 * Made by nija123098 on 8/13/2017.
 */
public class CustomCommandRegexCommand extends AbstractCommand {
    public CustomCommandRegexCommand() {
        super(CustomCommandCommand.class, "ragex", null, null, null, "Adds a regex search to activate a command on text without a prefix");
    }
    @Command
    public void command(Guild guild, String s){
        int space = s.indexOf(" ");
        if (space == -1) throw new ArgumentException("Please use the following syntax: <name> <regex>");
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
