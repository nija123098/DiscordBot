package com.github.kaaz.emily.helping.poll;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.anotations.Argument;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.exeption.ArgumentException;

/**
 * Made by nija123098 on 6/16/2017.
 */
public class PollVoteRetractCommand extends AbstractCommand {
    public PollVoteRetractCommand() {
        super(PollVoteCommand.class, "retract", null, null, null, "Retracts a vote on a poll given a specific poll ID");
    }
    @Command
    public void command(@Argument(info = "The poll ID") Integer pollID, @Argument(info = "The poll option") Integer option, User user){
        Poll poll = Poll.POOL_ID_MAP.get(pollID);
        if (poll == null)throw new ArgumentException("Unknown poll ID: " + pollID);
        poll.retractVote(user, option);
    }
}
