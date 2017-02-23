package com.github.kaaz.discordbot.config;

import com.github.kaaz.discordbot.util.Log;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * Made by nija123098 on 2/20/2017.
 */
public class ConfigHandler {
    private static final List<AbstractConfig> ABSTRACT_CONFIGS;
    static {
        ABSTRACT_CONFIGS = new ArrayList<>();
        EnumSet.allOf(ConfigLevel.class).forEach(configLevel -> {
            Reflections reflections = new Reflections("com.guthub.kaaz.discordbot.config.configs." + configLevel.name().toLowerCase());
            Set<Class<? extends AbstractConfig>> classes = reflections.getSubTypesOf(AbstractConfig.class);
            classes.forEach(clazz -> {
                try {
                    AbstractConfig config = clazz.newInstance();
                    config.setConfigLevel(configLevel);
                    ABSTRACT_CONFIGS.add(config);
                } catch (InstantiationException | IllegalAccessException e) {
                    Log.log("Exception during init of a config: " + clazz.getSimpleName(), e);
                }
            });
        });
    }
    // TODO SQL stuff goes here, more or less
    private static String getConfig(Configurable configurable, String configName){
        return null;
    }
    private static String[] getMulitConfig(Configurable configurable, String configName){
        return null;
    }
    public static void set(Configurable configurable, String configName, String...value){

    }
}
