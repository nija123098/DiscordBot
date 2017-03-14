package com.github.kaaz.emily.favor;

import com.github.kaaz.emily.config.ConfigLevel;
import com.github.kaaz.emily.config.Configurable;
import com.github.kaaz.emily.config.configs.user.FavorConfig;

/**
 * The handler for favor levels for guilds and users.
 *
 * @author nija123098
 * @since 2.0.0
 * @see FavorLevel
 */
public class FavorHandler {
    /**
     * A getter for the FavorLevel config.
     *
     * @param configurable the guild or user to get the favor amount for
     * @return the favor amount
     */
    public static Float getFavorAmount(Configurable configurable){
        if (configurable.getConfigLevel() != ConfigLevel.USER || configurable.getConfigLevel() != ConfigLevel.GUILD){
            return null;
        }
        return configurable.getSetting(FavorConfig.class);
    }

    /**
     * Gets the enum by the favor amount indicated.
     *
     * @param configurable the guild or user to get the favor level
     * @return the corresponding favor enum
     */
    public static FavorLevel getFavorLevel(Configurable configurable){
        if (configurable.getConfigLevel() != ConfigLevel.USER || configurable.getConfigLevel() != ConfigLevel.GUILD){
            return null;
        }
        return FavorLevel.getFavorLevel(configurable.getSetting(FavorConfig.class));
    }

    /**
     * A helper method to add favor to
     *
     * @param configurable the configurable to change the favor level for
     * @param amount the amount to change it by
     */
    public static void addFavorLevel(Configurable configurable, float amount){
        setFavorLevel(configurable, amount + configurable.getSetting(FavorConfig.class));
    }

    /**
     * A helper to set the favor level
     *
     * @param configurable the configurable to set the favor level for
     * @param amount the amouth to set it to
     */
    public static void setFavorLevel(Configurable configurable, float amount){
        if (amount < 0){
            amount *= 2;
        }
        configurable.setSetting(FavorConfig.class, amount);
    }
}
