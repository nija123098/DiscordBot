package com.github.kaaz.emily.fun.slot.packs;

import com.github.kaaz.emily.fun.slot.AbstractSlotPack;
import com.github.kaaz.emily.util.Rand;

/**
 * Made by nija123098 on 8/4/2017.
 */
public class NoirSlotPack extends AbstractSlotPack {
    public NoirSlotPack() {
        super(new String[]{"gun", "spy", "busts_in_silhouette", "bust_in_silhouette", "movie_camera", "projector", "knife", "dagger", "newspaper", "newspaper2", "boom"}, 0, 10, 2, 0, 2, 0, 1, 0, 1, 0, 0);
    }
    @Override
    public int[][] getTable() {
        int[][] ints = new int[3][3];
        for (int i = 0; i < ints.length; ++i) {
            ints[i][0] = Rand.getRand(this.vals.length - 2);
            for (int j = 1; j < ints[i].length; j++) {
                ints[i][j] = (ints[i][j - 1] + 1) % (this.vals.length - 1);
            }
        }
        for (int j = 0; j < 3; j++) {
            for (int i = 1; i < 3; i++) {
                if (ints[i][j] == 0) {
                    ints[i - 1][j] = 10;
                }
            }
        }
        return ints;
    }
    @Override
    public int getReturn(int[][] ints) {
        int[][] copy = new int[3][3];
        for (int i = 0; i < 3; i++) System.arraycopy(ints[i], 0, copy[i], 0, 3);
        for (int i = 0; i < 3; i++) for (int j = 0; j < 3; j++){
            if (copy[i][j] == 3) copy[i][j] = 2;
            else if (copy[i][j] == 5) copy[i][j] = 4;
            else if (copy[i][j] == 7) copy[i][j] = 6;
            else if (copy[i][j] == 9) copy[i][j] = 8;
        }
        int ret = super.getReturn(copy);
        if (ret == 0){
            for (int i = 0; i < 3; i++) {
                if (copy[0][i] == 0) return 2;
            }
        }
        return ret;
    }
}
