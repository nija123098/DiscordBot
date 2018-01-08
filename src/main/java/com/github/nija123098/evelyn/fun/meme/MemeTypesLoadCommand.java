package com.github.nija123098.evelyn.fun.meme;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;

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
