package com.github.nija123098.evelyn.fun.starboard;

import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;

import java.awt.*;
import java.util.concurrent.atomic.AtomicReference;

import static com.github.nija123098.evelyn.config.ConfigHandler.getSetting;
import static com.github.nija123098.evelyn.util.EmoticonHelper.getChars;
import static com.github.nija123098.evelyn.util.GraphicsHelper.getGradient;
import static java.awt.Color.WHITE;

/**
 * @author nija123098
 * @since 1.0.0
 */
public enum StarLevel {
    SPARKLE("star"),// 0xFFFFFF
    DWARF("star2"),// 0xFFF96F
    YELLOW("dizzy"),// 0xFFFB3A
    SOLAR("sunny"),// 0xFFEC00
    EXPLOSIVE("boom"),;//something

    private String emoticon;

    StarLevel(String emoticon) {
        this.emoticon = getChars(emoticon, false);
    }

    public Color getColor() {
        return getGradient(this.ordinal() / (float) values().length, WHITE, Color.YELLOW);
    }

    public String getEmoticon() {
        return this.emoticon;
    }

    public static StarLevel level(int count, Guild guild) {
        AtomicReference<StarLevel> set = new AtomicReference<>();
        getSetting(StarLevelRequirementConfig.class, guild).forEach((level, amount) -> {
            if (count > amount && (set.get() == null || set.get().ordinal() < level.ordinal())) set.set(level);
        });
        return set.get();
    }
}
