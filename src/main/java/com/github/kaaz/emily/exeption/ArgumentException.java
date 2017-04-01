package com.github.kaaz.emily.exeption;

/**
 * Made by nija123098 on 3/31/2017.
 */
public class ArgumentException extends BotException {
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

    @Override
    public String getMessage(){
        return "argument " + this.parameter + " - " + super.getMessage();
    }
}
