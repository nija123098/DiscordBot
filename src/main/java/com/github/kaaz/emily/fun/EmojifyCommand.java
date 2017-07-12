package com.github.kaaz.emily.fun;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.util.EmoticonHelper;
import com.github.kaaz.emily.util.StringIterator;

/**
 * Made by nija123098 on 5/11/2017.
 */
public class EmojifyCommand extends AbstractCommand {
    public EmojifyCommand() {
        super("emojify", ModuleLevel.FUN, null, null, "Turns you input into emojies");
    }
    @Command
    public void command(String args, MessageMaker maker){
        StringBuilder builder = new StringBuilder();
        new StringIterator(args).forEachRemaining(character -> builder.append(getChars(character)));
        maker.appendRaw(builder.toString());
    }
    private String getChars(char c){
        if (Character.isLetter(c)) return EmoticonHelper.getChars("regional_indicator_" + Character.toLowerCase(c));
        if (Character.isDigit(c)) return EmoticonHelper.getChars(Character.getName(c).substring(6));
        if (c == ' ') return EmoticonHelper.getChars("white_small_square");
        if (c == '?') return EmoticonHelper.getChars("grey_question");
        if (c == '!') return EmoticonHelper.getChars("grey_exclamation");
        return String.valueOf(c);
    }
}
