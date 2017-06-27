package com.github.kaaz.emily.exeption;

import com.github.kaaz.emily.command.annotations.Argument;
import com.github.kaaz.emily.command.annotations.LaymanName;

import java.lang.reflect.Parameter;

/**
 * Made by nija123098 on 3/31/2017.
 */
public class ArgumentException extends BotException {
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

    @Override
    public String getMessage(){
        if (args == null){
            return super.getMessage();
        }
        String s = "Argument " + this.parameter + " - " + super.getMessage() + "\nExpected: ";
        for (int i = 0; i < this.args.length; i++) {
            if (!this.args[i].isAnnotationPresent(Argument.class)){
                continue;
            }
            s += this.args[i].getClass().isAnnotationPresent(LaymanName.class) ? this.args[i].getAnnotation(LaymanName.class).value() : this.args[i].getType().getSimpleName();
            if (i != this.args.length - 1){
                s += ", ";
            }
        }
        s += "\n" + (this.args[this.parameter].isAnnotationPresent(LaymanName.class) ? this.args[this.parameter].getAnnotation(LaymanName.class).help() : this.args[this.parameter].getClass().getSimpleName() + " in the form of mention, ID, or reference");
        return s;
    }
}
