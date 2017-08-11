package com.github.kaaz.emily.template.commands;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.command.annotations.Context;
import com.github.kaaz.emily.discordobjects.wrappers.Message;

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
