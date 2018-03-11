package com.github.nija123098.evelyn.fun.meme;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class MemeTypesLoadCommand extends AbstractCommand {
    public MemeTypesLoadCommand() {
        super(MemeTypesCommand.class, "reload", "meme load, meme reload", null, "load", "Reloads meme types");
    }
    @Command
    public void command() {
        MemeTypesCommand.loadMemeTypes();
    }
}
