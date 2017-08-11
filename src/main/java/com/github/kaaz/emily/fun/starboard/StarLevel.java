package com.github.kaaz.emily.fun.starboard;

import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.util.EmoticonHelper;
import com.github.kaaz.emily.util.GraphicsHelper;

import java.awt.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Made by nija123098 on 5/31/2017.
 */
public enum StarLevel {
    SPARKLE("star"),// 0xFFFFFF
    DWARF("star2"),// 0xFFF96F
    YELLOW("dizzy"),// 0xFFFB3A
    SOLAR("sunny"),;// 0xFFEC00
    private String emoticon;
    StarLevel(String emoticon) {
        this.emoticon = EmoticonHelper.getChars(emoticon, false);
    }
    public Color getColor() {
        return GraphicsHelper.getGradient(this.ordinal() / (float) StarLevel.values().length, Color.WHITE, Color.YELLOW);
    }
    public String getEmoticon() {
        return this.emoticon;
    }
    public static StarLevel level(int count, Guild guild){
        AtomicReference<StarLevel> set = new AtomicReference<>();
        ConfigHandler.getSetting(StarLevelRequirementConfig.class, guild).forEach((level, amount) -> {
            if (count > amount && (set.get() == null || set.get().ordinal() < level.ordinal())) set.set(level);
        });
        return set.get();
    }
}
