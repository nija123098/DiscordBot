package com.github.nija123098.evelyn.config.configs.guild;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.template.KeyPhrase;
import com.github.nija123098.evelyn.template.Template;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.nija123098.evelyn.config.ConfigCategory.GUILD_PERSONALIZATION;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class GuildTemplatesConfig extends AbstractConfig<Map<KeyPhrase, List<Template>>, Guild> {
    public GuildTemplatesConfig() {
        super("guild_templates", "", GUILD_PERSONALIZATION, new HashMap<>(), "All global templates");
    }
}
