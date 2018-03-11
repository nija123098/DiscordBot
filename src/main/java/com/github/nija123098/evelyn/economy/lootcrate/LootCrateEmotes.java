package com.github.nija123098.evelyn.economy.lootcrate;

import com.github.nija123098.evelyn.util.EmoticonHelper;

/**
 * @author Dxeo
 * @since 1.0.0
 */
public class LootCrateEmotes {

    //constructor block
    private final String emote;
    LootCrateEmotes(String emote) {
        this.emote = emote;
    }

    //box emote
    public final static String BOX = EmoticonHelper.getChars("package",false);

    //loot crate emote
    public final static String CRATE = EmoticonHelper.getChars("gift",false);


}