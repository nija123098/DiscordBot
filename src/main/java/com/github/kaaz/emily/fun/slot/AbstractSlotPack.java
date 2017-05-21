package com.github.kaaz.emily.fun.slot;

import com.github.kaaz.emily.util.EmoticonHelper;

/**
 * Made by nija123098 on 5/19/2017.
 */
public class AbstractSlotPack {
    protected String[] chars;
    private int[] vals;
    public AbstractSlotPack(String[] chars, int...vals) {
        this.chars = new String[chars.length];
        for (int i = 0; i < this.chars.length; i++) {
            this.chars[i] = EmoticonHelper.getChars(chars[i]);
        }
        this.vals = vals;
    }
    public int length(){
        return this.vals.length;
    }
    public int getAmount(int i){
        return this.vals[i];
    }
    public String getChar(int i) {
        return this.chars[i];
    }
}
