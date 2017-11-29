package com.github.nija123098.evelyn.audio.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;

public class GreetingsVoiceConfig extends AbstractConfig<Boolean, Guild> {
    public GreetingsVoiceConfig() {
        super("greetings_voice", "Greet User Voice", ConfigCategory.GUILD_PERSONALIZATION, true, "If Evelyn greets you when she joins the channel");
    }
}
