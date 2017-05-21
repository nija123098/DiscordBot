package com.github.kaaz.emily.fun.slot;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 5/17/2017.
 */
public class SlotPackConfig extends AbstractConfig<SlotPack, User> {
    public SlotPackConfig() {
        super("slot_pack", BotRole.USER, SlotPack.DEFAULT, "The slot pack active");
    }
}
