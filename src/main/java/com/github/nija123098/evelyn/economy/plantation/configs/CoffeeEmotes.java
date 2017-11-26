package com.github.nija123098.evelyn.economy.plantation.configs;

import com.github.nija123098.evelyn.util.EmoticonHelper;

/**
 * Written by Dxeo 26/11/2017
 */

public class CoffeeEmotes {

    //constructor block
    private final String emote;
    CoffeeEmotes(String emote){
        this.emote = emote;
    }

    //beans emote
    public final static String BEANS = EmoticonHelper.getEmoji("bean").toString();

    //grounds emote
    public final static String GROUNDS = EmoticonHelper.getEmoji("grind").toString();

    //roasted emote
    public final static String ROASTED = EmoticonHelper.getEmoji("roast").toString();

}