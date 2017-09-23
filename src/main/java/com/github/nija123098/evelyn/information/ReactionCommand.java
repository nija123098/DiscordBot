package com.github.nija123098.evelyn.information;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.exeption.ArgumentException;
import com.github.nija123098.evelyn.util.EmoticonHelper;
import sx.blah.discord.handle.impl.obj.ReactionEmoji;

/**
 * Made by nija123098 on 7/9/2017.
 */
public class ReactionCommand extends AbstractCommand {
    public ReactionCommand() {
        super("reaction", ModuleLevel.FUN, "react, re", null, "Shows the image form of an emoji, or takes the image form and converts it to text");
    }
    @Command
    public void command(@Argument(info = "text") String s, MessageMaker maker){
        ReactionEmoji emoji = EmoticonHelper.getReactionEmoji(s);
        String display;
        if (emoji == null) {
            display = EmoticonHelper.getName(s);
            if (display == null) throw new ArgumentException("Please specify a reaction by name or symbol");
        }else display = emoji.toString().replace("<", "<:");// this is a patch for D4J
        maker.appendRaw(display);
    }

    @Override
    protected String getLocalUsages() {
        return "reaction <text> // converts the text to an emoji, or vice versa";
    }
}
