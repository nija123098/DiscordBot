package com.github.nija123098.evelyn.moderation;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.Message;

import static com.github.nija123098.evelyn.command.ModuleLevel.ADMINISTRATIVE;
import static com.github.nija123098.evelyn.moderation.MessageDeleteService.delete;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class PurgeCommand extends AbstractCommand {
    public PurgeCommand() {
        super("purge", ADMINISTRATIVE, null, null, "Deletes a certain number of messages");
    }

    @Command
    public void command(@Argument Integer count, Channel channel, Message message, Guild guild) {
        delete(channel.getMessages(count));
        message.delete();
    }
}
