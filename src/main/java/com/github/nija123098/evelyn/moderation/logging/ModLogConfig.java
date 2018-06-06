package com.github.nija123098.evelyn.moderation.logging;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.util.FormatHelper;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class ModLogConfig extends AbstractConfig<Channel, Guild> {
    public ModLogConfig() {
        super("mod_log", "Mod Log", ConfigCategory.LOGGING, guild -> guild.getChannels().stream().filter(channel -> FormatHelper.filtering(channel.getName().toLowerCase(), Character::isLetter).contains("modlog")).filter(Channel::canPost).findFirst().orElse(null), "The channel log of moderator actions");
    }
}
