package com.github.nija123098.evelyn.favor;

import com.github.nija123098.evelyn.config.Configurable;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.BotEvent;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventDistributor;
import com.github.nija123098.evelyn.util.ThreadHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class FavorChangeEvent implements BotEvent {
    private static final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor(r -> ThreadHelper.getDemonThreadSingle(r, "Favor-Change"));
    private static final Map<Configurable, Future<?>> FUTURE_MAP = new HashMap<>();
    public static void process(Configurable configurable, Runnable runnable) {
        float amount = FavorHandler.getFavorAmount(configurable);
        runnable.run();
        Future<?> future = FUTURE_MAP.put(configurable, EXECUTOR_SERVICE.schedule(() -> EventDistributor.distribute(new FavorChangeEvent(configurable, amount, FavorHandler.getFavorAmount(configurable))), 250, TimeUnit.MILLISECONDS));
        if (future != null) future.cancel(false);
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
