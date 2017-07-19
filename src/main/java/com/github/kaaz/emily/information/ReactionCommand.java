package com.github.kaaz.emily.information;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.exeption.ArgumentException;
import com.github.kaaz.emily.util.EmoticonHelper;

/**
 * Made by nija123098 on 7/9/2017.
 */
public class ReactionCommand extends AbstractCommand {
    public ReactionCommand() {
        super("reaction", ModuleLevel.FUN, "re", null, "Shows a reaction");
    }
    @Command
    public void command(String s, MessageMaker maker){
        String display = EmoticonHelper.getChars(s);
        if (display == null) {
            display = EmoticonHelper.getName(s);
            if (display == null) throw new ArgumentException("Please specify a reaction by name or symbol");
        }
        maker.appendRaw(display);
    }
}
