package com.github.nija123098.evelyn.exception;

import com.github.nija123098.evelyn.command.ContextRequirement;

/**
 * Thrown when insufficient context has been given to infer an desired output.
 *
 * @author nija123098
 * @since 1.0.0
 */
public class ContextException extends UserIssueException {
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
