package com.github.nija123098.evelyn.fun.slot.packs;

import com.github.nija123098.evelyn.fun.slot.AbstractSlotPack;

/**
 * Made by nija123098 on 5/19/2017.
 */
public class DefaultSlotPack extends AbstractSlotPack {
    public DefaultSlotPack() {
        super(new String[]{"seven", "crown", "bell", "chocolate_bar", "cherries", "melon"}, 7, 4, 3, 2, 2, 2);
    }
    @Override
    public int getReturn(int[][] ints) {
        int ret = super.getReturn(ints);
        if (ret == 0) {
            int count = 0;
            for (int i = 0; i < 3; i++) if (ints[i][1] == 0) ++count;
            return count;
        }
        return ret;
    }
}
