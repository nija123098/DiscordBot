package com.github.kaaz.discordbot.command;

import com.github.kaaz.discordbot.util.Log;

import java.lang.reflect.Method;

/**
 * Made by nija123098 on 2/20/2017.
 */
public abstract class AbstractCommand {
    private Method method;
    private Class<?>[] args;
    AbstractCommand(){
        Method[] methods = this.getClass().getMethods();
        for (Method m : methods) {
            if (m.isAnnotationPresent(Command.class)) {
                this.method = m;
                break;
            }
        }
        if (this.method == null){
            Log.log("No method annotated " + Command.class.getSimpleName());
            return;
        }
        this.args = this.method.getParameterTypes();
    }
    public abstract ModuleLevel getModule();
}
