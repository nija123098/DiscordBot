package com.github.kaaz.emily.discordobjects.wrappers.event.botevents;

import com.github.kaaz.emily.config.Configurable;
import com.github.kaaz.emily.discordobjects.wrappers.event.BotEvent;
import com.github.kaaz.emily.favor.FavorLevel;

/**
 * Made by nija123098 on 3/26/2017.
 */
public class FavorLevelChangeEvent implements BotEvent {
    private FavorLevel oldLevel, newLevel;
    private Configurable configurable;
    public FavorLevelChangeEvent(Configurable configurable, FavorLevel oldLevel, FavorLevel newLevel) {
        this.configurable = configurable;
        this.oldLevel = oldLevel;
        this.newLevel = newLevel;
    }
    public Configurable getConfigurable() {
        return this.configurable;
    }
    public FavorLevel getOldLevel() {
        return this.oldLevel;
    }
    public FavorLevel getNewLevel() {
        return this.newLevel;
    }
}
