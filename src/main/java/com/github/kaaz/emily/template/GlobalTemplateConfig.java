package com.github.kaaz.emily.template;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.config.GlobalConfigurable;
import com.github.kaaz.emily.perms.BotRole;

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
