package com.github.nija123098.evelyn.favor;

import com.github.nija123098.evelyn.util.GraphicsHelper;

import java.awt.*;

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
    DISTRUSTED(-50000, "Should get banned"),
    DISLIKED(-25000, "Has deviated from the proper coarse"),
    WORRISOME(-500, "Leaning toward the dark side"),
    NEUTRAL(-50, "Is still deciding their path"),
    LIKED(100, "Leaning toward the good side"),
    FAVORED(1000, "Has embraced a good nature"),
    PREFERRED(100000, "Indication of a wonderful human being"),;
    private float amount;
    private String description;
    FavorLevel(float amount, String description) {
        this.amount = amount;
        this.description = description;
    }
    public String getDescription() {
        return this.description;
    }
    public Color getColor() {
        return GraphicsHelper.getGradient(this.ordinal() / (float) FavorLevel.values().length, Color.RED, Color.GREEN);
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
