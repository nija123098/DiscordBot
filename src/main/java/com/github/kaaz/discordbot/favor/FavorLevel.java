package com.github.kaaz.discordbot.favor;

/**
 * The enum to indicate the name
 * of the favor level that
 * the user has with Emily.
 *
 * The amount indicated is inclusive.
 *
 * @author nija123098
 * @since 2.0.0
 */
public enum FavorLevel {
    DISTRUSTED(-5000),
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

    /**
     * A getter for the associated enum
     * where the value is inclusive
     *
     * @param value the float value
     * @return the favor level enum for the amount
     */
    static FavorLevel getFavorLevel(float value){
        if (value < DISTRUSTED.amount){
            return DISTRUSTED;
        }
        if (value >= PREFERRED.amount){
            return PREFERRED;
        }
        for (int i = 0; i < values().length; i++) {
            if (value < values()[i].amount){
                return values()[i - 1];
            }
        }
        return FavorLevel.NEUTRAL;
    }
}
