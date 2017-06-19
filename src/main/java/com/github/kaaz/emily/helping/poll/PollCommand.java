package com.github.kaaz.emily.helping.poll;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ContextType;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.anotations.Argument;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.discordobjects.wrappers.Message;
import com.github.kaaz.emily.exeption.ArgumentException;
import com.github.kaaz.emily.util.FormatHelper;
import com.github.kaaz.emily.util.Time;

import java.util.Arrays;

/**
 * Made by nija123098 on 6/15/2017.
 */
public class PollCommand extends AbstractCommand {
    public PollCommand() {
        super("poll", ModuleLevel.HELPER, "poll create", null, "Propose a question and choices for the chat to vote on");
    }
    @Command
    public void command(Message message, @Argument(optional = true, replacement = ContextType.NONE, info = "secret voting") Boolean secret, @Argument Time time, @Argument(optional = true, info = "the max votes a user may have", replacement = ContextType.NONE) Integer maxVotes, @Argument(info = "the question and options separated by ;") String s){
        String[] strings = s.split(";");
        if (strings.length < 2) throw new ArgumentException("Polls must have at least 2 options");
        for (int i = 0; i < strings.length; i++) {
            strings[i] = FormatHelper.trimFront(strings[i].trim());
        }
        new Poll(strings[0], Arrays.copyOfRange(strings, 1, strings.length), time.schedualed(), maxVotes == null ? strings.length - 1 : maxVotes, message);
    }
}
