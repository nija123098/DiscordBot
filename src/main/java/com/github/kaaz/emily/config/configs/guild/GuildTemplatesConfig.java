package com.github.kaaz.emily.config.configs.guild;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.perms.BotRole;
import com.github.kaaz.emily.template.KeyPhrase;
import com.github.kaaz.emily.template.Template;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Made by nija123098 on 4/19/2017.
 */
public class GuildTemplatesConfig extends AbstractConfig<Map<KeyPhrase, List<Template>>, Guild> {
    public GuildTemplatesConfig() {
        super("guild_templates", BotRole.GUILD_TRUSTEE, new HashMap<>(), "All global templates");
    }
}
