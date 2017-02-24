package com.github.kaaz.discordbot.config;

import com.github.kaaz.discordbot.perms.BotRole;
import com.github.kaaz.discordbot.util.StringCoder;

import java.util.List;

/**
 * Made by nija123098 on 2/22/2017.
 */
public class AbstractMultiConfig extends AbstractConfig {
    public AbstractMultiConfig(String name, BotRole botRole, List<String> defaul, String description) {
        super(name, botRole, StringCoder.encode(defaul), description);
    }
    public List<String> getMultiDefault(){
        return StringCoder.decode(super.getDefault());
    }
}
