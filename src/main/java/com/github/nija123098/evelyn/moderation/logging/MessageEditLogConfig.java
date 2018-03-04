package com.github.nija123098.evelyn.moderation.logging;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Attachment;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordMessageEditEvent;

import java.awt.*;

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
        if (message.getOldMessage() == null || message.getChannel().isPrivate() || message.getOldMessage().getAuthor().isBot() || (channel = this.getValue(message.getGuild())) == null) return;
        MessageMaker maker = new MessageMaker(channel).withColor(Color.GRAY).withAuthor(message.getOldMessage().getAuthor());
        maker.append("Message from ").appendRaw(message.getAuthor().getDisplayName(message.getGuild())).appendRaw(" edited ").append("in ").appendRaw(message.getChannel().mention());
        maker.getNewFieldPart().withInline(false).withBoth("Previous", message.getOldMessageCleanedContent());
        maker.getNewFieldPart().withInline(false).withBoth("New", message.getNewMessageCleanedString());
        maker.getNote().appendRaw("ID: " + message.getOldMessage().getID());
        maker.withTimestamp(System.currentTimeMillis());
        Attachment attachment = message.getOldMessage().getAttachments().stream().filter(att -> att.getUrl().endsWith("gif") || att.getUrl().endsWith("webv")).findFirst().orElse(null);
        if (attachment != null) maker.withImage(attachment.getUrl());
        maker.send();
    }
}
