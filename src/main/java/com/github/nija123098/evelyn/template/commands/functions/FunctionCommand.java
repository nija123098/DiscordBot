package com.github.nija123098.evelyn.template.commands.functions;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.exeption.ArgumentException;
import com.github.nija123098.evelyn.exeption.DevelopmentException;
import com.github.nija123098.evelyn.util.ReflectionHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Made by nija123098 on 8/9/2017.
 */
public class FunctionCommand extends AbstractCommand {
    private static final Set<Class<?>> APPROVED_RETURNS = new HashSet<>(Arrays.asList(Number.class, String.class));
    public FunctionCommand() {
        super("function", ModuleLevel.NONE, null, null, "Calls a function in the source code for a configurable");
    }
    @Command
    public Object command(@Argument Object o, @Argument String s){
        Method method;
        try{method = o.getClass().getMethod(s);
        }catch(NoSuchMethodException e){throw new ArgumentException(e);}
        boolean approved = false;
        if (Character.isLowerCase(method.getReturnType().getSimpleName().charAt(0))) approved = true;
        else for (Class<?> clazz : ReflectionHelper.getAssignableTypes(method.getReturnType())) if (APPROVED_RETURNS.contains(clazz)){
            approved = true;
            break;
        }
        if (!approved) throw new ArgumentException("That method is not approved for use");
        try{return method.invoke(o);
        } catch (IllegalAccessException e) {
            throw new ArgumentException("method " + method.getName() + " is not approved for use");
        } catch (InvocationTargetException e) {
            throw new DevelopmentException(e);
        }
    }
}
