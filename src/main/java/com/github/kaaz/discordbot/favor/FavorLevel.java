package com.github.kaaz.discordbot.favor;

/**
 * Made by nija123098 on 2/20/2017.
 */
public enum FavorLevel {
    DISTRUSTED(-10000),
    DISLIKED(-1000),
    WORRISOME(-100),
    NEUTRAL(-10),
    LIKED(10),
    FAVORED(100),
    PREFERRED(10000),;
    private float amount;
    FavorLevel(float amount) {
        this.amount = amount;
    }
    public static FavorLevel getFavorLevel(float value){// this can be made better
        if (value < DISTRUSTED.amount){
            return DISTRUSTED;
        }
        if (value > PREFERRED.amount){
            return PREFERRED;
        }
        for (int i = 0; i < values().length; i++) {
            if (value < values()[i].amount){
                return values()[i - 1];
            }
        }
        return NEUTRAL;
    }
}
