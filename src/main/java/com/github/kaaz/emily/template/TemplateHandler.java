package com.github.kaaz.emily.template;

import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.GlobalConfigurable;
import com.github.kaaz.emily.config.configs.guild.GuildTemplatesConfig;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.util.Log;
import com.github.kaaz.emily.util.Rand;

import java.util.Collections;
import java.util.List;

/**
 * Made by nija123098 on 2/27/2017.
 */
public class TemplateHandler {
    public static final char LEFT_BRACE = '<', RIGHT_BRACE = '>', ARGUMENT_SPLITTER = '|', ARGUMENT_CHARACTER = '%';

    /**
     * Forces the initialization of this class
     */
    public static void initialize(){
        Log.log("Template Handler initialize");
    }

    public static Template getTemplate(KeyPhrase keyPhrase, Guild guild, List<Template> exemptions){
        List<Template> templates = guild != null ? ConfigHandler.getSetting(GuildTemplatesConfig.class, guild).get(keyPhrase) : Collections.emptyList();
        if (templates.isEmpty()) templates = ConfigHandler.getSetting(GlobalTemplateConfig.class, GlobalConfigurable.GLOBAL).get(keyPhrase);
        if (templates == null || templates.isEmpty()){
            Log.log("No templates found for KeyPhrase: " + keyPhrase.name());
            return null;
        }// should not throw an exception since nothing failed
        if (exemptions.size() >= templates.size()){
            return templates.get(Rand.getRand(templates.size() - 1));
        } else {
            while (true){// todo optimize
                Template template = templates.get(Rand.getRand(templates.size() - 1));
                if (!exemptions.contains(template)){
                    return template;
                }
            }
        }
    }
}
