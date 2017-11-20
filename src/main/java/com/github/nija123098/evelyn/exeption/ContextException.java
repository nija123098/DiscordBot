package com.github.nija123098.evelyn.exeption;

import com.github.nija123098.evelyn.command.ContextRequirement;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;

import java.awt.*;

/**
 * Thrown when insufficient context has been given to infer an desired output.
 *
 * @author nija123098
 * @since 1.0.0
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

    @Override
    public MessageMaker makeMessage(Channel channel) {
        return super.makeMessage(channel).withColor(new Color(255, 183, 76)).getTitle().clear().appendRaw("Invalid Context").getMaker();
    }
}
