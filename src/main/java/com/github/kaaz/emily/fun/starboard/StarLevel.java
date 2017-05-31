package com.github.kaaz.emily.fun.starboard;

import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.util.EmoticonHelper;

import java.awt.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Made by nija123098 on 5/31/2017.
 */
public enum StarLevel {
    SPARKLE(new Color(0xFFFFFF), "star"),
    DWARF(new Color(0xFFF96F), "star2"),
    YELLOW(new Color(0xFFFB3A), "dizzy"),
    SOLAR(new Color(0xFFEC00), "sunny"),;
    private Color color;
    private String emoticon;
    StarLevel(Color color, String emoticon) {
        this.color = color;
        this.emoticon = EmoticonHelper.getChars(emoticon);
    }
    public Color getColor() {
        return this.color;
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
