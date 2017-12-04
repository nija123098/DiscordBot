package com.github.nija123098.evelyn.favor.configs.balencing;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class VoiceTimeFavorFactorConfig extends AbstractConfig<Float, Guild> {
    public VoiceTimeFavorFactorConfig() {
        super("voice_favor_factor", "Voice Favor Factor", ConfigCategory.FAVOR, 5f, "The factor by which favor is bestowed on a guild user for 5 minutes of voice time");
    }
}
