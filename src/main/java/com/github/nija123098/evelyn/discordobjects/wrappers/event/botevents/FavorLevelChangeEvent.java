package com.github.nija123098.evelyn.discordobjects.wrappers.event.botevents;

import com.github.nija123098.evelyn.config.Configurable;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.BotEvent;
import com.github.nija123098.evelyn.favor.FavorLevel;

/**
 * @author nija123098
 * @since 1.0.0
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
