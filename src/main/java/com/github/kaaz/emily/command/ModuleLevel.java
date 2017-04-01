package com.github.kaaz.emily.command;

/**
 * An enum for every command module
 *
 * @author nija123098
 * @since 2.0.0
 */
public enum ModuleLevel {
    MUSIC,
    FUN,
    BOT_ADMINISTRATIVE,
    ADMINISTRATIVE,
    ECONOMY,
    DEVELOPMENT,
    NONE,;
    public static ModuleLevel getModuleLevel(String s){
        try {
            return valueOf(s.replace("_", " ").toUpperCase());
        } catch (Exception e){
            return NONE;
        }
    }
}
