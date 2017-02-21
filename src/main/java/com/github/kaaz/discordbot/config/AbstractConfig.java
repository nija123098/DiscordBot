package com.github.kaaz.discordbot.config;

/**
 * Made by nija123098 on 2/20/2017.
 */
public class AbstractConfig {
    private String defaul, description;
    private boolean userCanSet;
    private ConfigLevel configLevel;
    public AbstractConfig(ConfigLevel configLevel, boolean userCanSet, String defaul, String description) {
        this.configLevel = configLevel;
        this.userCanSet = userCanSet;
        this.defaul = defaul;
        this.description = description;
    }
    public String getName() {
        return this.getClass().getSimpleName();
    }
    public String getDescription() {
        return this.description;
    }
    public boolean isUserCanSet() {
        return this.userCanSet;
    }
    public String getDefault(){
        return this.defaul;
    }
    public ConfigLevel getConfigLevel(){
        return this.configLevel;
    }
}
