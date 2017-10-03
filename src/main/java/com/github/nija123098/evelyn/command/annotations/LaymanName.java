package com.github.nija123098.evelyn.command.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation for making an object's name
 * user understandable when it is used an argument.
 *
 * @author nija123098
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface LaymanName {
    String value();
    String help() default "";
}
