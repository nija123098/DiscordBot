package com.github.kaaz.emily.discordobjects.wrappers.event.botevents;

import com.github.kaaz.emily.discordobjects.wrappers.event.BotEvent;
import com.github.kaaz.emily.favor.FavorLevel;

/**
 * Made by nija123098 on 3/26/2017.
 */
public class FavorLevelChange implements BotEvent {
    private FavorLevel oldLevel, newLevel;
    public FavorLevelChange(FavorLevel oldLevel, FavorLevel newLevel) {
        this.oldLevel = oldLevel;
        this.newLevel = newLevel;
    }
    public FavorLevel getOldLevel() {
        return this.oldLevel;
    }
    public FavorLevel getNewLevel() {
        return this.newLevel;
    }
}
