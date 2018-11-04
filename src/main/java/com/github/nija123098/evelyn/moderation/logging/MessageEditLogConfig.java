package com.github.nija123098.evelyn.moderation.logging;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordMessageEditEvent;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class MessageEditLogConfig extends AbstractConfig<Channel, Guild> {
    public MessageEditLogConfig() {
        super("message_edit_log", "Message Edit Log", ConfigCategory.LOGGING, (Channel) null, "log message edits");
    }

    @EventListener
    public void handle(DiscordMessageEditEvent message) {
        Channel channel;
        User user = message.getAuthor();
        Channel messageChannel = message.getChannel();
        if (message.getOldMessage() == null || message.getOldMessage().getAuthor().isBot() || (channel = this.getValue(message.getGuild())) == null) return;
        MessageMaker maker = new MessageMaker(channel);
        Logging.MESSAGE_EDITED.messageLog(maker, messageChannel, user, message.getNewMessage(), message.getOldMessageCleanedContent(), message.getNewMessageCleanedContent());
    }
}
