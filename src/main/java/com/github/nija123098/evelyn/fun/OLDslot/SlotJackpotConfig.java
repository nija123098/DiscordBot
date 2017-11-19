package com.github.nija123098.evelyn.fun.OLDslot;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.GlobalConfigurable;

/**
 * Made by nija123098 on 5/18/2017.
 */
public class SlotJackpotConfig extends AbstractConfig<Integer, GlobalConfigurable> {
    private static int SEED = 100;
    public SlotJackpotConfig() {
        super("slot_jackpot", ConfigCategory.STAT_TRACKING, SEED, "The amount the Jackpot begins and is reset to");
    }
    public static float getAndResetPot(){
        float val = ConfigHandler.getSetting(SlotJackpotConfig.class, GlobalConfigurable.GLOBAL);
        ConfigHandler.setSetting(SlotJackpotConfig.class, GlobalConfigurable.GLOBAL, SEED);
        return val;
    }
}
