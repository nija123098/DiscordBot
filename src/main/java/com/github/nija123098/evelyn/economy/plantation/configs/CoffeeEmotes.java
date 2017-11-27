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

    //coffee emote
    public final static String COFFEE = EmoticonHelper.getEmoji("Coffee").toString();

    //beans emote
    public final static String BEANS = EmoticonHelper.getEmoji("bean").toString();

    //roasted beans emote
    public final static String ROASTBEANS = EmoticonHelper.getEmoji("roastbean").toString();

    //grounds emote
    public final static String GROUNDS = EmoticonHelper.getEmoji("coffeegrounds").toString();

    //grinder emote
    public final static String GRINDER = EmoticonHelper.getEmoji("grinder").toString();

    //brewer emote
    public final static String BREWER = EmoticonHelper.getEmoji("brewer").toString();

    //roaster emote
    public final static String ROASTER = EmoticonHelper.getEmoji("roaster").toString();

    //steeper emote
    public final static String STEEPER = EmoticonHelper.getEmoji("steeper").toString();

}