package com.github.kaaz.emily.information;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Argument;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.exeption.ArgumentException;
import com.github.kaaz.emily.util.EmoticonHelper;

/**
 * Made by nija123098 on 7/9/2017.
 */
public class ReactionCommand extends AbstractCommand {
    public ReactionCommand() {
        super("reaction", ModuleLevel.FUN, "react, re", null, "Shows the image form of an emoji, or takes the image form and converts it to text");
    }
    @Command
    public void command(@Argument(info = "text") String s, MessageMaker maker){
        String display = EmoticonHelper.getChars(s, false);
        if (display == null) {
            display = EmoticonHelper.getName(s);
            if (display == null) throw new ArgumentException("Please specify a reaction by name or symbol");
        }
        maker.appendRaw(display);
    }

    @Override
    protected String getLocalUsages() {
        return "reaction <text> // converts the text to an emoji, or vice versa";
    }
}
