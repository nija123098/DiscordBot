package com.github.kaaz.emily.economy;

import com.github.kaaz.emily.util.EmoticonHelper;

/**
 * Made by nija123098 on 5/16/2017.
 */
public enum ItemComponent {
    GAME_DIE("game_die"),;
    private String icon;
    ItemComponent(String icon) {
        this.icon = EmoticonHelper.getChars(icon);
    }
    public String getIcon() {
        return this.icon;
    }
}
