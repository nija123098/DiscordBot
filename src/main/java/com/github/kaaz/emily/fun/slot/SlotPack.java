package com.github.kaaz.emily.fun.slot;

import com.github.kaaz.emily.fun.slot.packs.DefaultSlotPack;
import com.github.kaaz.emily.fun.slot.packs.SpaceSlotPack;
import com.github.kaaz.emily.util.Log;

/**
 * Made by nija123098 on 5/17/2017.
 */
public enum SlotPack {// don't make names have spaces
    DEFAULT(DefaultSlotPack.class),
    SPACE(SpaceSlotPack.class),;// perhaps make Earth angle change
    private AbstractSlotPack pack;
    SlotPack(Class<? extends AbstractSlotPack> clazz){
        try {
            this.pack = clazz.newInstance();
        } catch (InstantiationException e) {
            Log.log("Error while loading slot pack", e);
        } catch (IllegalAccessException e) {
            Log.log("Improperly made slot pack", e);
        }
    }
    public int length(){
        return this.pack.length();
    }
    public int getAmount(int i){
        return this.pack.getAmount(i);
    }
    public String getChar(int i) {
        return this.pack.getChar(i);
    }
}
