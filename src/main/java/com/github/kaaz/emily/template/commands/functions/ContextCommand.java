package com.github.kaaz.emily.template.commands.functions;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ContextPack;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Argument;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.exeption.ArgumentException;
import com.github.kaaz.emily.exeption.DevelopmentException;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Made by nija123098 on 8/10/2017.
 */
public class ContextCommand extends AbstractCommand {
    private static final Map<String, Function<ContextPack, Object>> SUPPLIER_MAP = new HashMap<>();
    static {
        Stream.of(ContextPack.class.getMethods()).forEach(method -> SUPPLIER_MAP.put(method.getName().substring(3).toLowerCase(), pack -> {
            try{return method.invoke(pack);
            } catch (IllegalAccessException e) {
                throw new DevelopmentException("ContextCommand was not properly updated to match ContextPack", e);
            } catch (InvocationTargetException e) {
                throw new DevelopmentException("Something went honorably wrong", e);
            }
        }));
    }
    public ContextCommand() {
        super("context", ModuleLevel.NONE, null, null, "Provides context for the current command");
    }
    @Command
    public Object command(@Argument(info = "context name") String s, ContextPack pack){
        Function<ContextPack, Object> supplier = SUPPLIER_MAP.get(s.toLowerCase());
        if (supplier == null) throw new ArgumentException("Invalid context, available: " + SUPPLIER_MAP.keySet());
        return supplier.apply(pack);
    }
}
