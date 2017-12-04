package com.github.nija123098.evelyn.helping.poll;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.exception.ArgumentException;

import static com.github.nija123098.evelyn.helping.poll.Poll.POOL_ID_MAP;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class PollVoteCommand extends AbstractCommand {
    public PollVoteCommand() {
        super(PollCommand.class, "vote", null, null, null, "Votes on a poll given a specific poll ID");
    }

    @Command
    public void command(@Argument(info = "The poll ID") Integer pollID, @Argument(info = "The poll option") Integer option, User user) {
        Poll poll = POOL_ID_MAP.get(pollID);
        if (poll == null) throw new ArgumentException("Unknown poll ID: " + pollID);
        poll.vote(user, option);
    }
}
