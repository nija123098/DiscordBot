package com.github.nija123098.evelyn.favor;

import com.github.nija123098.evelyn.config.Configurable;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.BotEvent;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventDistributor;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class FavorChangeEvent implements BotEvent {
    public static void process(Configurable configurable, Runnable runnable) {
        float amount = FavorHandler.getFavorAmount(configurable);
        runnable.run();
        EventDistributor.distribute(new FavorChangeEvent(configurable, amount, FavorHandler.getFavorAmount(configurable)));
    }

    private Configurable configurable;
    private float oldLevel;
    private float newLevel;

    public FavorChangeEvent(Configurable configurable, float oldLevel, float newLevel) {
        this.configurable = configurable;
        this.oldLevel = oldLevel;
        this.newLevel = newLevel;
    }

    public Configurable getConfigurable() {
        return this.configurable;
    }

    public float getOldValue() {
        return this.oldLevel;
    }

    public float getNewValue() {
        return this.newLevel;
    }
}
