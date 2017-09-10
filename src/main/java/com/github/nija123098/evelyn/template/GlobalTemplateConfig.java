package com.github.nija123098.evelyn.template;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.GlobalConfigurable;
import com.github.nija123098.evelyn.perms.BotRole;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Made by nija123098 on 4/19/2017.
 */
public class GlobalTemplateConfig extends AbstractConfig<Map<KeyPhrase, List<Template>>, GlobalConfigurable> {
    public GlobalTemplateConfig() {
        super("global_templates", BotRole.BOT_ADMIN, "All global templates", guild -> new HashMap<>());
    }
    public boolean checkDefault(){
        return false;
    }
}