package com.github.kaaz.discordbot.config;

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
                    ABSTRACT_CONFIGS.add(clazz.newInstance());
                } catch (InstantiationException | IllegalAccessException e) {
                    System.out.println("Exception during init of a config: " + clazz.getSimpleName());
                    e.printStackTrace();
                }
            });
        });
    }
    public static void set(Configurable configurable, String configName, String value){
        //TODO
    }
}
