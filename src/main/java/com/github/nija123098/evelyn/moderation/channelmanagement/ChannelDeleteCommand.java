package com.github.nija123098.evelyn.moderation.channelmanagement;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.exception.PermissionsException;

import java.util.ConcurrentModificationException;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class ChannelDeleteCommand extends AbstractCommand {

    public ChannelDeleteCommand() {
        super(ChannelCommand.class, "delete", "cdelete", null, null, "Delete the mentioned channel, requires confirmation");
    }

    @Command
    public void command(@Argument Channel channel, MessageMaker maker, Channel invokeChannel) {
        maker.mustEmbed();
        maker.getTitle().appendRaw("Channel Deletion");
        maker.appendRaw("The " + channel.mention() + " channel will be deleted, this is an irreversible action and all messages in this channel will be deleted, use at your own risk");

        //command code
        maker.withReactionBehavior("red_tick", ((add, reaction, u) -> {
            //remove reactions
            try {
                maker.clearReactionBehaviors();
            } catch (ConcurrentModificationException ignored) { }
            maker.sentMessage().getReactions().forEach(reactions -> maker.sentMessage().removeReaction(reactions));

        }));

        //if send
        maker.withReactionBehavior("green_tick", ((add, reaction, u) -> {

            //remove reactions
            try {
                maker.clearReactionBehaviors();
            } catch (ConcurrentModificationException ignored) {
            }
            maker.sentMessage().getReactions().forEach(reactions -> maker.sentMessage().removeReaction(reactions));

            MessageMaker maker2 = new MessageMaker(invokeChannel);
            try {
                channel.delete();
            } catch (PermissionsException e) {
                maker2.appendRaw("I couldn't delete the `" + channel.getName() + "` channel, check your discord permissions to ensure I have permission to edit that channel.");
            }
            maker2.forceCompile().send();
        }));
        maker.forceCompile().send();
    }
}