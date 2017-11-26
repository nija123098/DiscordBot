package com.github.nija123098.evelyn.moderation;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;

import java.util.Arrays;

/**
 * Made by nija123098 on 5/10/2017.
 */
public class PurgeCommand extends AbstractCommand {
    public PurgeCommand() {
        super("purge", ModuleLevel.ADMINISTRATIVE, null, null, "Deletes a certain number of messages");
    }
    @Command
    public void command(@Argument Integer count, Channel channel){

        //oh hey a purge that works, would you look at that
        channel.channel().bulkDelete(Arrays.asList(channel.channel().getMessageHistory(count + 1).asArray()));

    }
}
