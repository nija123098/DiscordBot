package com.github.kaaz.emily.config;

import com.github.kaaz.emily.launcher.Launcher;
import com.github.kaaz.emily.perms.BotRole;
import com.github.kaaz.emily.service.services.ScheduleService;
import com.github.kaaz.emily.util.Care;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractCountingConfig<T extends Configurable> extends AbstractConfig<Integer, T> {
    private Map<T, Integer> add = new ConcurrentHashMap<>();
    private Map<T, Integer> valueBase = new ConcurrentHashMap<>();
    public AbstractCountingConfig(String name, BotRole botRole, String description) {
        super(name, botRole, 0, description);
        ScheduleService.scheduleRepeat(600_000, 600_000, this::save);
        Launcher.registerShutdown(this::save);
    }
    private void save(){
        this.add.forEach((t, integer) -> {
            Care.lessSleep(10);
            super.setValue(t, super.getValue(t) + integer);
            this.add.remove(t);
            this.valueBase.remove(t);
        });
    }
    @Override
    public Integer getValue(T configurable) {
        return this.valueBase.computeIfAbsent(configurable, super::getValue) + this.add.getOrDefault(configurable, 0);
    }
    @Override
    public Integer setValue(T configurable, Integer value) {
        return this.valueBase.computeIfAbsent(configurable, super::getValue) + this.add.compute(configurable, (t, integer) -> (integer == null ? 0 : integer) + (value - this.valueBase.get(t)));
    }
    @Override
    public Integer getDefault(T t) {
        return 0;
    }
    @Override
    public boolean checkDefault() {
        return false;
    }
}
