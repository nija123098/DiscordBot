package com.github.nija123098.evelyn.moderation.streamer;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordPresenceUpdate;

public class StreamerConfig extends AbstractConfig<Boolean, User> {

    public StreamerConfig() {
        super("is_streamer", "", ConfigCategory.STAT_TRACKING, false, "If the user has ever streamed");
    }
    @EventListener
    public void handle(DiscordPresenceUpdate update) {
        if (update.getNewPresence().getOptionalStreamingUrl().isPresent()) this.setValue(update.getUser(), true);
    }
}
