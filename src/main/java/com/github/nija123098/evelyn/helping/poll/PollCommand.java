package com.github.nija123098.evelyn.helping.poll;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.wrappers.Message;
import com.github.nija123098.evelyn.exception.ArgumentException;
import com.github.nija123098.evelyn.util.Time;

import static com.github.nija123098.evelyn.command.ContextType.NONE;
import static com.github.nija123098.evelyn.command.ModuleLevel.HELPER;
import static com.github.nija123098.evelyn.util.FormatHelper.trimFront;
import static java.lang.System.currentTimeMillis;
import static java.util.Arrays.copyOfRange;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class PollCommand extends AbstractCommand {
    public PollCommand() {
        super("poll", HELPER, "poll create", null, "Propose a question and choices for the chat to vote on");
    }

    @Command
    public void command(Message message, @Argument(optional = true, replacement = NONE) Time time, @Argument(optional = true, info = "max votes per user", replacement = NONE) Integer maxVotes, @Argument(info = "the question and options separated by ;") String s) {
        String[] strings = s.split(";");
        if (strings.length < 2)
            throw new ArgumentException("Polls must have at least a prompt and 2 options, split them with ;");
        for (int i = 0; i < strings.length; i++) {
            strings[i] = trimFront(strings[i].trim());
        }
        new Poll(strings[0], copyOfRange(strings, 1, strings.length), time == null ? currentTimeMillis() + 300_000 : time.schedualed(), maxVotes == null ? strings.length - 1 : maxVotes, message);
    }
}
