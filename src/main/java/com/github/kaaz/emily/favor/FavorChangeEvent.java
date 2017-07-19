package com.github.kaaz.emily.favor;

import com.github.kaaz.emily.config.Configurable;
import com.github.kaaz.emily.discordobjects.wrappers.event.BotEvent;

/**
 * Made by nija123098 on 7/19/2017.
 */
public class FavorChangeEvent implements BotEvent {
    private Configurable configurable;
    private float oldLevel;
    private float newLevel;
    FavorChangeEvent(Configurable configurable, float oldLevel, float newLevel) {
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
