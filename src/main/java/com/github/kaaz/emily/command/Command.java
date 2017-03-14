package com.github.kaaz.emily.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An method annotated with this annotation
 * in an class that extends AbstractCommand
 * will be the method invoked for the command
 *
 * @author nija123098
 * @since 2.0.0
 * @see AbstractCommand
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
}
