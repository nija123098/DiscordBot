package com.github.nija123098.evelyn.template.commands.functions;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.util.Time;

public class GetTimeCommand extends AbstractCommand {
    public GetTimeCommand() {
        super("get time", ModuleLevel.NONE, "gett", null, "Gets a command object from a string representation for custom commands");
    }
    @Command
    public Time command(@Argument String time) {
        return new Time(time);
    }
}
