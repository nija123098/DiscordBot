package com.github.kaaz.emily.command.annotations;

import com.github.kaaz.emily.command.ContextType;
import com.github.kaaz.emily.command.InvocationObjectGetter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotating a parameter in a command method
 * parses the args for the type and reduces
 * the args to remove the wrapped type
 * {@link String} arguments for argument passing.
 *
 * @author nija123098
 * @since 2.0.0
 * @see InvocationObjectGetter
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Argument {
    boolean optional() default false;
    ContextType replacement() default ContextType.DEFAULT;
    String info() default "";
}
