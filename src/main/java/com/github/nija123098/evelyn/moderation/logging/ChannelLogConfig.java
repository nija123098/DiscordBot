package com.github.nija123098.evelyn.moderation.logging;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordChannelCreate;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordChannelDelete;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordChannelUpdate;
import sx.blah.discord.handle.obj.IChannel;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class ChannelLogConfig extends AbstractConfig<Channel, Guild> {
    public ChannelLogConfig() {
        super("channel_edit_log", "Channel Edit Log", ConfigCategory.LOGGING, (Channel) null, "Log channel changes");
    }

    @EventListener
    public void handle(DiscordChannelCreate event) {
        Channel channel;
        if ((channel = this.getValue(event.getGuild())) == null) return;
        MessageMaker maker = new MessageMaker(channel);
        Logging.CHANNEL_CREATION.channelLog(maker, event.getChannel());
    }

    @EventListener
    public void handle(DiscordChannelDelete event) {
        Channel channel;
        if ((channel = this.getValue(event.getGuild())) == null || this.getValue(event.getGuild()).isDeleted()) return;
        MessageMaker maker = new MessageMaker(channel);
        Logging.CHANNEL_DELETION.channelLog(maker, event.getChannel());
    }

    @EventListener
    public void handle(DiscordChannelUpdate event) {
        Channel channel;
        IChannel oldChannel = event.getOldChannel(), newChannel = event.getNewChannel();
        String oldTopic = ((oldChannel.getTopic() == null || oldChannel.getTopic().isEmpty()) ? "none" : oldChannel.getTopic()), newTopic = ((newChannel.getTopic() == null || newChannel.getTopic().isEmpty()) ? "none" : newChannel.getTopic());
        if ((channel = this.getValue(event.getGuild())) == null) return;
        if (!oldChannel.getName().equalsIgnoreCase(newChannel.getName())) {
            MessageMaker maker = new MessageMaker(channel);
            Logging.CHANNEL_NAME_UPDATE.channelLog(maker, event.getChannel(), oldChannel.getName(), newChannel.mention());
        }
        if (oldChannel.isNSFW() != newChannel.isNSFW()) {
            MessageMaker maker = new MessageMaker(channel);
            Logging.CHANNEL_NSFW_UPDATE.channelLog(maker, event.getChannel(), String.valueOf(oldChannel.isNSFW()), String.valueOf(newChannel.isNSFW()));
        }
        if (!oldTopic.equals(newTopic)) {
            MessageMaker maker = new MessageMaker(channel);
            Logging.CHANNEL_TOPIC_UPDATE.channelLog(maker, event.getChannel(), ((oldChannel.getTopic() == null || oldChannel.getTopic().isEmpty()) ? "none" : oldChannel.getTopic()), ((newChannel.getTopic() == null || newChannel.getTopic().isEmpty()) ? "none" : newChannel.getTopic()));
        }
    }
}
