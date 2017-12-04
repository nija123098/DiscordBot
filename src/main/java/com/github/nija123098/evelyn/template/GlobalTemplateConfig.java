package com.github.nija123098.evelyn.template;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.GlobalConfigurable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class GlobalTemplateConfig extends AbstractConfig<Map<KeyPhrase, List<Template>>, GlobalConfigurable> {
    public GlobalTemplateConfig() {
        super("global_templates", "", ConfigCategory.STAT_TRACKING, new HashMap<>(), "All global templates");
    }
}
