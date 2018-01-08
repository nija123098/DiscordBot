package com.github.nija123098.evelyn.economy;

import com.github.nija123098.evelyn.util.EmoticonHelper;

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
        this.icon = EmoticonHelper.getChars(this.name().toLowerCase(), true);
    }
    ItemComponent(String icon) {
        this.icon = EmoticonHelper.getChars(icon, true);
    }
    public String getIcon() {
        return this.icon;
    }
}
