package com.github.kaaz.emily.template;

import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.GlobalConfigurable;
import com.github.kaaz.emily.config.configs.guild.GuildTemplatesConfig;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.exeption.ArgumentException;
import com.github.kaaz.emily.util.Log;
import com.github.kaaz.emily.util.Rand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * Made by nija123098 on 2/27/2017.
 */
public class TemplateHandler {
    static final char LEFT_BRACE = '<', RIGHT_BRACE = '>', ARGUMENT_SPLITTER = '|', ARGUMENT_CHARACTER = '%';

    /**
     * Forces the initialization of this class
     */
    public static void initialize(){
        Log.log("Template Handler initialize");
    }

    public static List<Template> getTemplates(KeyPhrase keyPhrase, Guild guild){
        List<Template> templates = guild != null ? ConfigHandler.getSetting(GuildTemplatesConfig.class, guild).get(keyPhrase) : Collections.emptyList();
        if (templates == null || templates.isEmpty()) templates = ConfigHandler.getSetting(GlobalTemplateConfig.class, GlobalConfigurable.GLOBAL).get(keyPhrase);
        return templates == null || templates.isEmpty() ? Collections.emptyList() : templates;
    }
    public static Template getTemplate(KeyPhrase keyPhrase, Guild guild, List<Template> exemptions){
        List<Template> templates = getTemplates(keyPhrase, guild);
        if (templates.isEmpty()){
            Log.log("No templates found for KeyPhrase: " + keyPhrase.name());
            return null;
        }// should not throw an exception since nothing failed
        if (exemptions.size() >= templates.size()) return Rand.getRand(templates, false);
        else return templates.get(Rand.getRand(templates.size() - 1, exemptions.toArray(new Integer[exemptions.size()])));
    }
    public static Template addTemplate(KeyPhrase keyPhrase, Guild guild, String s){
        Template template = new Template(s, keyPhrase.getDefinition());
        Consumer<Map<KeyPhrase, List<Template>>> consumer = v -> v.computeIfAbsent(keyPhrase, k -> new ArrayList<>()).add(template);
        if (guild == null) ConfigHandler.alterSetting(GlobalTemplateConfig.class, GlobalConfigurable.GLOBAL, consumer);
        else ConfigHandler.alterSetting(GuildTemplatesConfig.class, guild, consumer);
        return template;
    }
    public static Template removeTemplate(KeyPhrase keyPhrase, Guild guild, int i){
        AtomicReference<Template> removed = new AtomicReference<>();
        Consumer<Map<KeyPhrase, List<Template>>> consumer = v -> v.compute(keyPhrase, (k, templates) -> {
            if (templates == null || templates.size() <= i) throw new ArgumentException("There is no template at that index");
            removed.set(templates.remove(i));
            return templates;
        });
        if (guild == null) ConfigHandler.alterSetting(GlobalTemplateConfig.class, GlobalConfigurable.GLOBAL, consumer);
        else ConfigHandler.alterSetting(GuildTemplatesConfig.class, guild, consumer);
        return removed.get();
    }
}
