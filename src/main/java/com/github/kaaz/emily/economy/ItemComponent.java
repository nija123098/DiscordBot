package com.github.kaaz.emily.economy;

import com.github.kaaz.emily.util.EmoticonHelper;

/**
 * Made by nija123098 on 5/16/2017.
 */
public enum ItemComponent {
    GAME_DIE,
    KNIFE,
    DIAMOND("large_blue_diamond"),
    SPOON,
    BRIEFCASE,;
    private String icon;
    ItemComponent(){
        this.icon = EmoticonHelper.getChars(this.name().toLowerCase());
    }
    ItemComponent(String icon) {
        this.icon = EmoticonHelper.getChars(icon);
    }
    public String getIcon() {
        return this.icon;
    }
}
