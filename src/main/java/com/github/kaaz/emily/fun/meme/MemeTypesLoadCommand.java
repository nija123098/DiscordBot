package com.github.kaaz.emily.fun.meme;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.annotations.Command;

/**
 * Made by nija123098 on 6/4/2017.
 */
public class MemeTypesLoadCommand extends AbstractCommand {
    public MemeTypesLoadCommand() {
        super(MemeTypesCommand.class, "reload", "meme load, meme reload", null, "load", "Reloads meme types");
    }
    @Command
    public void command(){
        MemeTypesCommand.loadMemeTypes();
    }
}
