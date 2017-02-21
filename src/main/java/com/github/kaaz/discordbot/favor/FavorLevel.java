package com.github.kaaz.discordbot.favor;

/**
 * Made by nija123098 on 2/20/2017.
 */
public enum FavorLevel {
    DISTRUSTED(-10000),
    DSILIKED(-1000),
    WORRISOME(-100),
    NEUTRAL(-10),
    LIKED(10),
    FAVORED(100),
    PREFERED(10000),;
    private float amount;
    FavorLevel(float amount) {
        this.amount = amount;
    }

}
