package com.github.kaaz.emily.template.commands.functions;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Argument;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.exeption.ArgumentException;
import com.github.kaaz.emily.exeption.DevelopmentException;
import com.github.kaaz.emily.util.ReflectionHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Made by nija123098 on 8/9/2017.
 */
public class FunctionCommand extends AbstractCommand {
    private static final Set<Class<?>> APPROVED_RETURNS = new HashSet<>(Arrays.asList(Number.class, String.class, Integer.TYPE, Long.TYPE));
    public FunctionCommand() {
        super("function", ModuleLevel.NONE, null, null, "Calls a function in the source code for a configurable");
    }
    @Command
    public Object command(@Argument Object o, @Argument String s){
        Method method;
        try{method = o.getClass().getMethod(s);
        }catch(NoSuchMethodException e){throw new ArgumentException(e);}
        boolean approved = false;
        for (Class<?> clazz : ReflectionHelper.getAssignableTypes(method.getReturnType())) if (APPROVED_RETURNS.contains(clazz)){
            approved = true;
            break;
        }
        if (!approved) throw new ArgumentException("That method is not approved for use");
        try{return method.invoke(o);
        } catch (IllegalAccessException e) {
            throw new ArgumentException("That method is not approved for use");
        } catch (InvocationTargetException e) {
            throw new DevelopmentException(e);
        }
    }
}
