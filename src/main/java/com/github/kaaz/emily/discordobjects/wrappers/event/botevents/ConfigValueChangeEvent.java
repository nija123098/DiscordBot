package com.github.kaaz.emily.discordobjects.wrappers.event.botevents;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.config.Configurable;
import com.github.kaaz.emily.discordobjects.wrappers.event.BotEvent;

/**
 * Made by nija123098 on 4/27/2017.
 */
public class ConfigValueChangeEvent<V, E extends Configurable> implements BotEvent {
    private E configurable;
    private AbstractConfig<V, E> config;
    private V oldValue, newValue;
    public ConfigValueChangeEvent(E configurable, AbstractConfig<V, E> config, V oldValue, V newValue) {
        this.configurable = configurable;
        this.config = config;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }
    public E getConfigurable() {
        return this.configurable;
    }
    public AbstractConfig<V, E> getConfig() {
        return this.config;
    }
    public Class<AbstractConfig<V, E>> getConfigType() {
        return (Class<AbstractConfig<V, E>>) this.config.getClass();
    }
    public V getOldValue() {
        return this.oldValue;
    }
    public V getNewValue() {
        return this.newValue;
    }
}
