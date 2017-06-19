package com.github.kaaz.emily.audio.configs;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.config.GlobalConfigurable;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 6/12/2017.
 */
public class RequiredPlaysToDownloadConfig extends AbstractConfig<Integer, GlobalConfigurable> {
    public RequiredPlaysToDownloadConfig() {
        super("required_to_download", BotRole.BOT_ADMIN, 0, "The required plays set monthly");
    }
}
