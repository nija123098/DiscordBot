package com.github.nija123098.evelyn.template;

import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.GlobalConfigurable;
import com.github.nija123098.evelyn.config.configs.guild.GuildTemplatesConfig;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.exception.ArgumentException;
import com.github.nija123098.evelyn.util.Log;
import com.github.nija123098.evelyn.util.LogColor;
import com.github.nija123098.evelyn.util.Rand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * Handles template getting, registration, and de-registration.
 *
 * {@link Template} defaults are stored in {@link GlobalTemplateConfig}.
 * {@link Template} guild specific overrides {@link GuildTemplatesConfig}.
 *
 * @author nija123098
 * @since 1.0.0
 * @see Template
 */
public class TemplateHandler {

    /**
     * Forces the initialization of this class.
     */
    public static void initialize(){
        Log.log(LogColor.blue("Template Handler initialized.") + LogColor.yellow(" Jet fuel can't melt these steel beams."));
    }

    /**
     * Gets a list of {@link Template}s, by default these are templates
     * made by the bot admins, otherwise are {@link Guild} overrides.
     *
     * @param keyPhrase the {@link KeyPhrase} to get templates for.
     * @param guild the {@link Guild} in the context of getting templates.
     * @return a list of {@link Template}s, default there are no {@link Guild} overrides.
     */
    public static List<Template> getTemplates(KeyPhrase keyPhrase, Guild guild){
        List<Template> templates = guild != null ? ConfigHandler.getSetting(GuildTemplatesConfig.class, guild).get(keyPhrase) : Collections.emptyList();
        if (templates == null || templates.isEmpty()) templates = ConfigHandler.getSetting(GlobalTemplateConfig.class, GlobalConfigurable.GLOBAL).get(keyPhrase);
        return templates == null || templates.isEmpty() ? Collections.emptyList() : templates;
    }

    /**
     * Gets a list of {@link Template}s, by default these are templates
     * made by the bot admins, otherwise are {@link Guild} overrides.
     *
     * @param keyPhrase the {@link KeyPhrase} to get templates for.
     * @param guild the {@link Guild} in the context of getting templates.
     * @param exemptions the templates to not include in the list.
     * @return a list of {@link Template}s, default there are no {@link Guild} overrides,
     * not including the listed exemptions.
     */
    public static Template getTemplate(KeyPhrase keyPhrase, Guild guild, List<Template> exemptions){
        List<Template> templates = getTemplates(keyPhrase, guild);
        if (templates.isEmpty()){
            Log.log("No templates found for KeyPhrase: " + keyPhrase.name());
            return null;
        }// should not throw an exception since nothing failed
        if (exemptions.size() >= templates.size()) return Rand.getRand(templates, false);
        else return templates.get(Rand.getRand(templates.size(), exemptions.toArray(new Integer[exemptions.size()])));
    }

    /**
     * Registers a template as a default or {@link Guild} override depending on the guild input.
     *
     * @param keyPhrase the {@link KeyPhrase} to define the {@link CustomCommandDefinition}
     *                  and for registration and usage.
     * @param guild the guild to add the {@link Template} to for overriding defaults.
     * @param s the text to compile as a {@link Template}.
     * @throws RuntimeException if the {@link Template} did not compile.
     * @return the compiled {@link Template}.
     */
    public static Template addTemplate(KeyPhrase keyPhrase, Guild guild, String s){
        Template template = new Template(s, keyPhrase.getDefinition());
        Consumer<Map<KeyPhrase, List<Template>>> consumer = v -> v.computeIfAbsent(keyPhrase, k -> new ArrayList<>()).add(template);
        if (guild == null) ConfigHandler.alterSetting(GlobalTemplateConfig.class, GlobalConfigurable.GLOBAL, consumer);
        else ConfigHandler.alterSetting(GuildTemplatesConfig.class, guild, consumer);
        return template;
    }

    /**
     * Removes a template from the registry of templates, either
     * {@link Guild} override or default depending on the guild input.
     *
     * @param keyPhrase the {@link KeyPhrase} to remove the {@link Template} for.
     * @param guild the {@link Guild} context to remove the {@link Template} from.
     * @param i the index of the {@link Template} in the
     *        {@link GuildTemplatesConfig} or {@link GlobalTemplateConfig}.
     * @return the compiled {@link Template} that was removed.
     */
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
