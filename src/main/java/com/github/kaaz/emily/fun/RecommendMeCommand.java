package com.github.kaaz.emily.fun;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Argument;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;

import java.util.Optional;

/**
 * Written by Soarnir 26/7/17
 */

public class RecommendMeCommand extends AbstractCommand {
    public RecommendMeCommand() {
        super("recommend me", ModuleLevel.FUN,null,null,"I'll try to recommend you something!");
    }
    @Command
    public void command(@Argument(optional = true) String arg, MessageMaker maker) {

    }
}
