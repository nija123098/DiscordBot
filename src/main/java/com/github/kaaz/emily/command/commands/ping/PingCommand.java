package com.github.kaaz.emily.command.commands.ping;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;

/**
 * Made by nija123098 on 3/30/2017.
 */
public class PingCommand extends AbstractCommand {
    public PingCommand() {
        super("ping", null, "ping_pong");
    }
    @Command
    public void command(MessageMaker helper){
        helper.append("ping").withUserColor();
    }
}
