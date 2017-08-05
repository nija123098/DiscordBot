package com.github.kaaz.emily.fun.slot;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.GlobalConfigurable;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 5/18/2017.
 */
public class SlotJackpotConfig extends AbstractConfig<Integer, GlobalConfigurable> {
    private static int SEED = 100;
    public SlotJackpotConfig() {
        super("slot_jackpot", BotRole.BOT_ADMIN, SEED, "The amount the Jackpot begins and is reset to");
    }
    public static float getAndResetPot(){
        float val = ConfigHandler.getSetting(SlotJackpotConfig.class, GlobalConfigurable.GLOBAL);
        ConfigHandler.setSetting(SlotJackpotConfig.class, GlobalConfigurable.GLOBAL, SEED);
        return val;
    }
}
