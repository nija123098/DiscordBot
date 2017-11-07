package com.github.nija123098.evelyn.fun.call;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;

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
