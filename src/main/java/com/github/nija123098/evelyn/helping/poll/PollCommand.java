package com.github.nija123098.evelyn.helping.poll;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ContextType;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.wrappers.Message;
import com.github.nija123098.evelyn.exeption.ArgumentException;
import com.github.nija123098.evelyn.util.FormatHelper;
import com.github.nija123098.evelyn.util.Time;

import java.util.Arrays;

/**
 * Made by nija123098 on 6/15/2017.
 */
public class PollCommand extends AbstractCommand {
    public PollCommand() {
        super("poll", ModuleLevel.HELPER, "poll create", null, "Propose a question and choices for the chat to vote on");
    }
    @Command
    public void command(Message message, @Argument(optional = true, replacement = ContextType.NONE) Time time, @Argument(optional = true, info = "max votes per user", replacement = ContextType.NONE) Integer maxVotes, @Argument(info = "the question and options separated by ;") String s){
        String[] strings = s.split(";");
        if (strings.length < 2) throw new ArgumentException("Polls must have at least a prompt and 2 options, split them with ;");
        for (int i = 0; i < strings.length; i++) {
            strings[i] = FormatHelper.trimFront(strings[i].trim());
        }
        new Poll(strings[0], Arrays.copyOfRange(strings, 1, strings.length), time == null ? System.currentTimeMillis() + 300_000 : time.schedualed(), maxVotes == null ? strings.length - 1 : maxVotes, message);
    }
}
