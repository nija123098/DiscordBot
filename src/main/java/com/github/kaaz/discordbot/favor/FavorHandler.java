package com.github.kaaz.discordbot.favor;

import com.github.kaaz.discordbot.config.ConfigLevel;
import com.github.kaaz.discordbot.config.Configurable;
import com.github.kaaz.discordbot.config.configs.user.FavorConfig;

/**
 * Made by nija123098 on 2/20/2017.
 */
public class FavorHandler {
    public static FavorLevel getFavorLevel(Configurable configurable){
        if (configurable.getConfigLevel() != ConfigLevel.USER | configurable.getConfigLevel() != ConfigLevel.GUILD){
            return null;
        }
        float value = configurable.getSetting(FavorConfig.class);
        if (value < FavorLevel.DISTRUSTED.amount){
            return FavorLevel.DISTRUSTED;
        }
        if (value > FavorLevel.PREFERRED.amount){
            return FavorLevel.PREFERRED;
        }
        for (int i = 0; i < FavorLevel.values().length; i++) {
            if (value < FavorLevel.values()[i].amount){
                return FavorLevel.values()[i - 1];
            }
        }
        return FavorLevel.NEUTRAL;
    }
    public static void addFavorLevel(Configurable configurable, float amount){
        setFavorLevel(configurable, amount + configurable.getSetting(FavorConfig.class));
    }
    public static void setFavorLevel(Configurable configurable, float amount){
        if (amount < 0){
            amount *= 2;
        }
        configurable.setSetting(FavorConfig.class, amount);
    }
}
