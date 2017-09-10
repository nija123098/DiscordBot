package com.github.nija123098.evelyn.fun.starboard;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.perms.BotRole;
import com.github.nija123098.evelyn.util.FormatHelper;

/**
 * Made by nija123098 on 5/31/2017.
 */
public class StarBoardLocationConfig extends AbstractConfig<Channel, Guild> {
    public StarBoardLocationConfig() {
        super("star_board", BotRole.GUILD_TRUSTEE, "The location of the starboard", guild -> {
            for (Channel channel : guild.getChannels()) if (FormatHelper.filtering(channel.getName(), Character::isLetterOrDigit).toLowerCase().equals("starboard")) return channel;
            return null;
        });
    }
}
