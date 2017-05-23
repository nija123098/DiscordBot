package com.github.kaaz.emily.command;

import com.github.kaaz.emily.command.anotations.LaymanName;

/**
 * An enum for every command module
 *
 * @author nija123098
 * @since 2.0.0
 */
@LaymanName(value = "Module", help = "A group of commands.  MUSIC, FUN, BOT_ADMINISTRATIVE, ADMINISTRATIVE, ECONOMY, DEVELOPMENT, INFO, HELPER, NONE")
public enum ModuleLevel {
    MUSIC,
    FUN,
    BOT_ADMINISTRATIVE,
    ADMINISTRATIVE,
    ECONOMY,
    DEVELOPMENT,
    INFO,
    HELPER,
    NONE,;
    public static ModuleLevel getModuleLevel(String s){
        try {
            return valueOf(s.replace("_", " ").toUpperCase());
        } catch (Exception e){
            return NONE;
        }
    }
}
