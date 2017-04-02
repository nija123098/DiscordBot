package com.github.kaaz.emily.config;

/**
 * The class for the global configurable
 */
public class GlobalConfigurable implements Configurable {
    /**
     * The global configurable object
     * for access to global configs
     */
    public static final GlobalConfigurable GLOBAL = new GlobalConfigurable();

    private GlobalConfigurable(){}
    @Override
    public String getID() {
        return "GLOBAL-id";
    }
    @Override
    public ConfigLevel getConfigLevel() {
        return ConfigLevel.GLOBAL;
    }
}
