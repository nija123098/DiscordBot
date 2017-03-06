package com.github.kaaz.discordbot.command;

/**
 * An enum for every command module
 *
 * @author nija123098
 * @since 2.0.0
 */
public enum ModuleLevel {
    MUSIC,
    FUN,
    BOT_AMDINISTRATIVE,
    ADMINISTRATIVE,
    ECONOMY,
    DEVELOPMENT,
    NONE,;
    public static ModuleLevel getModualLevel(String s){
        try {
            return valueOf(s.replace("_", " ").toUpperCase());
        } catch (Exception e){
            return NONE;
        }
    }
}
