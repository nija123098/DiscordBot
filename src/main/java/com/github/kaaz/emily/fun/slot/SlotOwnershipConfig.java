package com.github.kaaz.emily.fun.slot;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.perms.BotRole;

import java.util.HashMap;
import java.util.Map;

/**
 * Made by nija123098 on 5/17/2017.
 */
public class SlotOwnershipConfig extends AbstractConfig<Map<SlotPack, Boolean>, User> {
    public SlotOwnershipConfig() {
        super("slot_pack_ownership", BotRole.BOT_ADMIN, defaul(), "The slot packs a user owns");
    }
    private static Map<SlotPack, Boolean> defaul(){
        Map<SlotPack, Boolean> map = new HashMap<>();
        map.put(SlotPack.DEFAULT, true);
        return map;
    }
}
