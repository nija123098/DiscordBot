package com.github.nija123098.evelyn.fun.slot;

import com.github.nija123098.evelyn.util.EmoticonHelper;
import com.github.nija123098.evelyn.util.Rand;

/**
 * Made by nija123098 on 5/19/2017.
 */
public class AbstractSlotPack {
    protected String[] chars;
    protected int[] vals;
    public AbstractSlotPack(String[] chars, int...vals) {
        this.chars = new String[chars.length];
        for (int i = 0; i < this.chars.length; i++) {
            this.chars[i] = EmoticonHelper.getChars(chars[i], true);
        }
        this.vals = vals;
    }
    public int getAmount(int i){
        return this.vals[i];
    }
    public int getReturn(int[][] ints) {
        Integer slot = null;
        if ((ints[0][0] == ints[1][1] && ints[1][1] == ints[2][2]) || (ints[2][0] == ints[1][1] && ints[1][1] == ints[0][2])) slot = ints[1][1];
        else if (ints[0][0] == ints[1][0] && ints[1][0] == ints[2][0]) slot = ints[1][1];
        return slot == null ? 0 : this.vals[slot];
    }
    public String getChar(int i) {
        return this.chars[i];
    }
    public int[][] getTable() {
        int[][] ints = new int[3][3];
        for (int i = 0; i < ints.length; ++i) {
            ints[i][0] = Rand.getRand(this.vals.length);
            for (int j = 1; j < ints[i].length; j++) {
                ints[i][j] = (ints[i][j - 1] + 1) % this.vals.length;
            }
        }
        return ints;
    }
}
