package com.github.nija123098.evelyn.economy.currencytransfer;

import com.github.nija123098.evelyn.util.EmoticonHelper;

/**
 * @author nija123098
 * @since 1.0.0
 */
public enum ItemComponent {
    GAME_DIE,
    KNIFE,
    DIAMOND("large_blue_diamond"),
    SPOON,
    BRIEFCASE,;
    private String icon;
    ItemComponent() {
        this.icon = EmoticonHelper.getChars(this.name().toLowerCase(), true);
    }
    ItemComponent(String icon) {
        this.icon = EmoticonHelper.getChars(icon, true);
    }
    public String getIcon() {
        return this.icon;
    }
}
