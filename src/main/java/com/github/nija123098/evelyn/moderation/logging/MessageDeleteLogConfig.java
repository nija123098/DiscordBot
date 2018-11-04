package com.github.nija123098.evelyn.moderation.logging;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Attachment;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordMessageDelete;

import java.awt.*;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class MessageDeleteLogConfig extends AbstractConfig<Channel, Guild> {
    public MessageDeleteLogConfig() {
        super("message_delete_log", "Message Delete Log", ConfigCategory.LOGGING, (Channel) null, "The location logs should be made for messages that are deleted");
    }
    @EventListener(queueSize = 150)
    public void handle(DiscordMessageDelete message) {
        Channel channel;
        User user = message.getAuthor();
        Channel messageChannel = message.getChannel();
        if (message.getMessage() == null || message.getChannel().isPrivate() || message.getMessage().getAuthor().isBot() || (channel = this.getValue(message.getGuild())) == null) return;
        MessageMaker maker = new MessageMaker(channel);
        Logging.MESSAGE_DELETED.messageLog(maker, messageChannel, user, message.getMessage());
    }
}
