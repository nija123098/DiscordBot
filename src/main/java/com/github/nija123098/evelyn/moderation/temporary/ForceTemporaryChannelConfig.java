package com.github.nija123098.evelyn.moderation.temporary;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.VoiceChannel;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordVoiceJoin;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class ForceTemporaryChannelConfig extends AbstractConfig<Boolean, Guild> {
    public ForceTemporaryChannelConfig() {
        super("redirect_to_temporary", "Force Move to Co-op", ConfigCategory.GAME_TEMPORARY_CHANNELS, false, "Redirects the user to the correct temporary channel");
    }
    @EventListener
    public void handle(DiscordVoiceJoin join){
        if (ConfigHandler.getSetting(TemporaryGameChannelsConfig.class, join.getGuild()) != 0 && ConfigHandler.getSetting(TemporaryChannelConfig.class, join.getChannel())){
            String play = join.getUser().getPresence().getPlayingText();
            if (play == null) return;
            String channelName = TemporaryGameChannelsConfig.playTextToChannel.apply(play);
            if (join.getChannel().getName().equals(channelName)) return;
            for (VoiceChannel channel : join.getGuild().getVoiceChannels()){
                if (channel.getName().equals(channelName)) join.getUser().moveToVoiceChannel(channel);
            }
        }
    }
}
