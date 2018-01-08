package com.github.nija123098.evelyn.fun.slot;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Made by nija123098 on 5/17/2017.
 */
public class SlotOwnershipConfig extends AbstractConfig<Map<SlotPack, Boolean>, User> {
    public SlotOwnershipConfig() {
        super("current_money", "slot_pack_ownership", ConfigCategory.STAT_TRACKING, defaul(), "The slot packs a user owns");
    }
    private static Map<SlotPack, Boolean> defaul(){
        Map<SlotPack, Boolean> map = new HashMap<>();
        map.put(SlotPack.DEFAULT, true);
        return map;
    }
}
