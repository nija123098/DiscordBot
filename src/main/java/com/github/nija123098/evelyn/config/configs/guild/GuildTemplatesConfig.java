package com.github.nija123098.evelyn.config.configs.guild;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.perms.BotRole;
import com.github.nija123098.evelyn.template.KeyPhrase;
import com.github.nija123098.evelyn.template.Template;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Made by nija123098 on 4/19/2017.
 */
public class GuildTemplatesConfig extends AbstractConfig<Map<KeyPhrase, List<Template>>, Guild> {
    public GuildTemplatesConfig() {
        super("guild_templates", ConfigCategory.GUILD_PERSONALIZATION, guild -> new HashMap<>(), "All global templates");
    }
}
