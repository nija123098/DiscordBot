package com.github.nija123098.evelyn.exeption;

import com.github.nija123098.evelyn.config.Configurable;

/**
 * Made by nija123098 on 5/15/2017.
 */
public class ConfigurableConvertException extends DevelopmentException {
    public ConfigurableConvertException(Class<? extends Configurable> from, Class<? extends Configurable> to) {
        super("Can not convert this configurable from " + from.getName() + " to " + to.getName());
    }
}
