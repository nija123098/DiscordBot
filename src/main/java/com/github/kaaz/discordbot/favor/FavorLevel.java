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
    float amount;
    FavorLevel(float amount) {
        this.amount = amount;
    }
}
