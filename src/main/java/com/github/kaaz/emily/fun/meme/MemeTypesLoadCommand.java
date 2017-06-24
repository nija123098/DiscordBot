package com.github.kaaz.emily.fun.meme;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;

/**
 * Made by nija123098 on 6/4/2017.
 */
public class MemeTypesLoadCommand extends AbstractCommand {
    public MemeTypesLoadCommand() {
        super(MemeTypesCommand.class, "reload", "meme load, meme reload", null, "load", "Reloads meme types");
    }
    @Command
    public void command(MessageMaker maker){
        MemeTypesCommand.loadMemeTypes();
    }
}
