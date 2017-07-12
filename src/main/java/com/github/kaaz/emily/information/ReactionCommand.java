package com.github.kaaz.emily.information;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.discordobjects.wrappers.Message;

/**
 * Made by nija123098 on 7/9/2017.
 */
public class ReactionCommand extends AbstractCommand {
    public ReactionCommand() {
        super("reaction", ModuleLevel.FUN, "re", null, "Shows a reaction");
    }
    @Command
    public void command(String s, Message message){
        message.addReactionByName(s);
    }
}
