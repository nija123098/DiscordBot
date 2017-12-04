package com.github.nija123098.evelyn.moderation.logging;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;

import static com.github.nija123098.evelyn.config.ConfigCategory.LOGGING;
import static com.github.nija123098.evelyn.util.FormatHelper.filtering;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class ModLogConfig extends AbstractConfig<Channel, Guild> {
    public ModLogConfig() {
        super("mod_log", "Mod Log", LOGGING, guild -> guild.getChannels().stream().filter(channel -> filtering(channel.getName().toLowerCase(), Character::isLetter).contains("modlog")).findFirst().orElse(null), "The channel log of moderator actions");
    }
}
