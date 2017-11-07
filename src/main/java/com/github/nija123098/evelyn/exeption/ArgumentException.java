package com.github.nija123098.evelyn.exeption;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.LaymanName;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;

import java.lang.reflect.Parameter;

/**
 * Should be thrown when an improper argument has
 * been passed and the user should be notified.
 *
 * @author nija123098
 * @since 1.0.0
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
        StringBuilder s = new StringBuilder("Argument " + (this.parameter + 1) + " - " + super.getMessage() + "\nExpected arguments:  ");
        for (Parameter arg : this.args) {
            if (!arg.isAnnotationPresent(Argument.class)) continue;
            s.append(arg.getClass().isAnnotationPresent(LaymanName.class) ? arg.getAnnotation(LaymanName.class).value() : arg.getType().getSimpleName() + ", ");
        }
        return s.substring(0, s.length() - 2);
    }

    @Override
    public MessageMaker makeMessage(Channel channel) {
        return super.makeMessage(channel).getNote().append("Please check the help command for more information on how to use this command.").getMaker();
    }
}
