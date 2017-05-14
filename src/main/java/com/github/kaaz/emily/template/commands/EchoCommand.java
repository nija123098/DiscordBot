package com.github.kaaz.emily.template.commands;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.command.anotations.Argument;

/**
 * Made by nija123098 on 4/23/2017.
 */
public class EchoCommand extends AbstractCommand {
    public EchoCommand() {
        super("echo", ModuleLevel.NONE, null, null, "Repeats the argument back");
    }
    @Command
    public String echo(@Argument(optional = true) String s){
        return s;
    }
}
