package com.github.nija123098.evelyn.template.commands.functions;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.command.annotations.Context;
import com.github.nija123098.evelyn.discordobjects.wrappers.Message;

/**
 * Made by nija123098 on 8/10/2017.
 */
public class DeleteInvocationCommand extends AbstractCommand {
    public DeleteInvocationCommand() {
        super("delinvo", ModuleLevel.NONE, null, null, "Deletes the invoking message, is no source safe");
    }
    @Command
    public void command(@Context(softFail = true) Message message){
        message.delete();
    }
}
