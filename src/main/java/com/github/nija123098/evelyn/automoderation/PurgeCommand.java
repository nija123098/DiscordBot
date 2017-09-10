package com.github.nija123098.evelyn.automoderation;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;

/**
 * Made by nija123098 on 5/10/2017.
 */
public class PurgeCommand extends AbstractCommand {
    public PurgeCommand() {
        super("purge", ModuleLevel.ADMINISTRATIVE, null, null, "Deletes old messages");
        this.okOnSuccess = false;
    }
    @Command
    public void command(@Argument Integer count, Channel channel){
        MessageDeleteService.delete(channel.getMessages(count + 1));
    }
}
