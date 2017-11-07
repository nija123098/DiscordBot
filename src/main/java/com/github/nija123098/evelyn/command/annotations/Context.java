package com.github.nija123098.evelyn.command.annotations;

import com.github.nija123098.evelyn.command.ContextType;
import com.github.nija123098.evelyn.command.InvocationObjectGetter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A parameter annotated with this argument
 * results in the command method being
 * passed the related context data for that
 * argument type, as assigned in
 * {@link InvocationObjectGetter}.
 *
 * By default the values passed is the first
 * conversion assigned to that type.
 *
 * @author nija123098
 * @since 1.0.0
 * @see InvocationObjectGetter
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Context {
    ContextType value() default ContextType.DEFAULT;
    boolean softFail() default false;
}
