package com.github.nija123098.evelyn.moderation;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.Message;

/**
 * Made by nija123098 on 5/10/2017.
 */
public class PurgeCommand extends AbstractCommand {
    public PurgeCommand() {
        super("purge", ModuleLevel.ADMINISTRATIVE, null, null, "Deletes a certain number of messages");
    }
    @Command
    public void command(@Argument Integer count, Channel channel, Message message, Guild guild){
        MessageDeleteService.delete(channel.getMessages(count));
        message.delete();
    }
}
