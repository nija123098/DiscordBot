package com.github.nija123098.evelyn.moderation.logging;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Attachment;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordMessageDelete;

import java.awt.*;

public class MessageDeleteLogConfig extends AbstractConfig<Channel, Guild> {
    public MessageDeleteLogConfig() {
        super("message_delete_log", "Massage Delete Log", ConfigCategory.LOGGING, (Channel) null, "The location logs should be made for messages that are deleted");
    }
    @EventListener
    public void handle(DiscordMessageDelete delete){
        Channel channel;
        if (delete.getMessage() == null || delete.getChannel().isPrivate() || delete.getMessage().getAuthor().isBot() || (channel = this.getValue(delete.getGuild())) == null) return;
        MessageMaker maker = new MessageMaker(channel).withColor(Color.GRAY).withAuthor(delete.getMessage().getAuthor()).append("Message deleted from ").appendRaw(delete.getAuthor().getDisplayName(delete.getGuild())).appendRaw(" ").append("in ").appendRaw(delete.getChannel().mention()).appendRaw("\n" + delete.getMessage().getMentionCleanedContent());
        maker.getNote().appendRaw("ID: " + delete.getMessage().getID());
        Attachment attachment = delete.getMessage().getAttachments().stream().filter(att -> att.getUrl().endsWith("gif") || att.getUrl().endsWith("webv")).findFirst().orElse(null);
        if (attachment != null) maker.withImage(attachment.getUrl());
        maker.send();
    }
}
