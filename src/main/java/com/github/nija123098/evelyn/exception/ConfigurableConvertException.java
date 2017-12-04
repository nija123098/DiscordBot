package com.github.nija123098.evelyn.exception;

import com.github.nija123098.evelyn.config.Configurable;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class ConfigurableConvertException extends DevelopmentException {
    public ConfigurableConvertException(Class<? extends Configurable> from, Class<? extends Configurable> to) {
        super("Can not convert this configurable from " + from.getName() + " to " + to.getName());
    }
}
