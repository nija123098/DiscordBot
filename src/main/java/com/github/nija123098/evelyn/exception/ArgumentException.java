package com.github.nija123098.evelyn.exception;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.LaymanName;

import java.lang.reflect.Parameter;

/**
 * Should be thrown when an improper argument has
 * been passed and the user should be notified.
 *
 * @author nija123098
 * @since 1.0.0
 */
public class ArgumentException extends UserIssueException {
    private AbstractCommand command;
    private Parameter[] args;
    private int parameter;

    public ArgumentException(String message) {
        super(message);
    }

    public ArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public ArgumentException(Throwable cause) {
        super(cause);
    }

    public int getParameter() {
        return this.parameter;
    }

    public void setParameter(int parameter) {
        this.parameter = parameter;
    }

    public Parameter[] getArgs() {
        return args;
    }

    public void setArgs(Parameter[] args) {
        this.args = args;
    }

    public void setImproperUsage(int parameter, Parameter[] args, AbstractCommand command) {
        this.parameter = parameter;
        this.args = args;
        this.command = command;
    }

    @Override
    public String getMessage() {
        if (args == null) {
            return super.getMessage();
        }
        StringBuilder s = new StringBuilder("Argument " + (this.parameter + 1) + " - " + super.getMessage() + "\nExpected arguments:  ");
        for (Parameter arg : this.args) {
            if (!arg.isAnnotationPresent(Argument.class)) continue;
            s.append(arg.getClass().isAnnotationPresent(LaymanName.class) ? arg.getAnnotation(LaymanName.class).value() : arg.getType().getSimpleName() + ", ");
        }
        return s.substring(0, s.length() - 2);
    }
}
