package com.github.kaaz.emily.fun.call;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.Channel;

/**
 * Made by nija123098 on 6/19/2017.
 */
public class CallHangUpCommand extends AbstractCommand {
    public CallHangUpCommand() {
        super(CallCommand.class, "hangup", "hangup, hang up", null, null, "Stops the current call");
    }
    @Command
    public void command(Channel channel, MessageMaker maker){
        CallCommand.hangUp(channel);
        maker.append("Your call has been hung up");
    }
}
