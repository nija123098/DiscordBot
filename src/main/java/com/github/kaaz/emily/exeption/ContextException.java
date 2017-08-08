package com.github.kaaz.emily.exeption;

import com.github.kaaz.emily.command.ContextRequirement;

/**
 * Made by nija123098 on 4/10/2017.
 */
public class ContextException extends BotException {
    public ContextException() {
        super("You are not in the correct context to use this");
    }
    public static void checkRequirement(Object requirement, ContextRequirement type){
        if (requirement == null) throw new ContextException(type + ": " + type.getErrorMessage());
    }
    public ContextException(Class<?> clazz) {
        super("This command needs to have a " + clazz.getSimpleName() + " in it's context");
    }
    public ContextException(String message) {
        super(message);
    }
}
