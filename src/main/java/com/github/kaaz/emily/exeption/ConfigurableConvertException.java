package com.github.kaaz.emily.exeption;

import com.github.kaaz.emily.config.Configurable;

/**
 * Made by nija123098 on 5/15/2017.
 */
public class ConfigurableConvertException extends ArgumentException {
    public ConfigurableConvertException(Class<? extends Configurable> from, Class<? extends Configurable> to) {
        super("Can not convert this configurable from " + from.getName() + " to " + to.getName());
    }
}
