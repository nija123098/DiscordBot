package com.github.kaaz.emily.fun.starboard;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.discordobjects.wrappers.Channel;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.perms.BotRole;
import com.github.kaaz.emily.util.FormatHelper;

/**
 * Made by nija123098 on 5/31/2017.
 */
public class StarBoardLocationConfig extends AbstractConfig<Channel, Guild> {
    public StarBoardLocationConfig() {
        super("star_board", BotRole.GUILD_TRUSTEE, "The location of the starboard", guild -> {
            for (Channel channel : guild.getChannels()) if (FormatHelper.alphaNumeric(channel.getName()).toLowerCase().equals("starboard")) return channel;
            return null;
        });
    }
}
