package com.github.nija123098.evelyn.fun.OLDslot;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

/**
 * Made by nija123098 on 5/17/2017.
 */
public class SlotPackConfig extends AbstractConfig<SlotPack, User> {
    public SlotPackConfig() {
        super("slot_pack", ConfigCategory.PERSONAL_PERSONALIZATION, SlotPack.DEFAULT, "The slot pack active");
    }
}
