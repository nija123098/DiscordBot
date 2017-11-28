package com.github.nija123098.evelyn.util;

/**
 * Written by Soarnir 27/11/17
 */

public class GeneralEmotes {

    //constructor block
    private final String emote;
    GeneralEmotes(String emote){
        this.emote = emote;
    }

    //empty emote
    public final static String EMPTY = EmoticonHelper.getEmoji("EMPTY").toString();

    //green tick emote
    public final static String GREENTICK = EmoticonHelper.getEmoji("GreenTick").toString();

    //red tick emote
    public final static String REDTICK = EmoticonHelper.getEmoji("RedTick").toString();

}
