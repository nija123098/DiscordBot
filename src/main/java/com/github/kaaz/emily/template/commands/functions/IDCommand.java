package com.github.kaaz.emily.template.commands.functions;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.command.annotations.Argument;
import com.github.kaaz.emily.config.Configurable;

/**
 * Made by nija123098 on 4/25/2017.
 */
public class IDCommand extends AbstractCommand {
    public IDCommand() {
        super("id", ModuleLevel.NONE, null, null, "Gets the id of a configurable");
    }
    @Command
    public String command(@Argument Configurable configurable){
        return configurable.getID();
    }
}
