package com.github.kaaz.emily.command.anotations;

import com.github.kaaz.emily.command.InvocationObjectGetter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A parameter annotated with this argument
 * results in the command method being
 * passed the related context data for that
 * argument type, as assigned in {@link InvocationObjectGetter}.
 *
 * By default the values passed is the first
 * conversion assigned to that type.
 *
 * @author nija123098
 * @since 2.0.0
 * @see InvocationObjectGetter
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Context {
    String value() default "";
}
