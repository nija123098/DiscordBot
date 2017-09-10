package com.github.nija123098.evelyn.exeption;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.LaymanName;

import java.lang.reflect.Parameter;

/**
 * Made by nija123098 on 3/31/2017.
 */
public class ArgumentException extends BotException {
    private AbstractCommand command;
    private Parameter[] args;
    private int parameter;
    public ArgumentException() {
    }

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

    public void setImproperUsage(int parameter, Parameter[] args, AbstractCommand command){
        this.parameter = parameter;
        this.args = args;
        this.command = command;
    }

    @Override
    public String getMessage(){
        if (args == null){
            return super.getMessage();
        }
        StringBuilder s = new StringBuilder("Argument " + this.parameter + " - " + super.getMessage() + "\nExpected: ");
        for (int i = 0; i < this.args.length; i++) {
            if (!this.args[i].isAnnotationPresent(Argument.class)){
                continue;
            }
            s.append(this.args[i].getClass().isAnnotationPresent(LaymanName.class) ? this.args[i].getAnnotation(LaymanName.class).value() : this.args[i].getType().getSimpleName());
            if (i != this.args.length - 1){
                s.append(", ");
            }
        }
        s.append("\n").append(this.args[this.parameter].isAnnotationPresent(LaymanName.class) ? this.args[this.parameter].getAnnotation(LaymanName.class).help() : this.args[this.parameter].getClass().getSimpleName() + " in the form of mention, ID, or reference");
        return s.toString();
    }
}
