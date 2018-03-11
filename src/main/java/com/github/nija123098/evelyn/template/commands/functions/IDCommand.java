package com.github.nija123098.evelyn.template.commands.functions;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.Configurable;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class IDCommand extends AbstractCommand {
    public IDCommand() {
        super("id", ModuleLevel.NONE, null, null, "Gets the id of a configurable");
    }
    @Command
    public String command(@Argument Configurable configurable) {
        return configurable.getID();
    }
}
