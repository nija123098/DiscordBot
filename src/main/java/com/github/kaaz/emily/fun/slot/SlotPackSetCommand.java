package com.github.kaaz.emily.fun.slot;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.anotations.Argument;
import com.github.kaaz.emily.command.anotations.Command;

/**
 * Made by nija123098 on 5/17/2017.
 */
public class SlotPackSetCommand extends AbstractCommand {
    public SlotPackSetCommand() {
        super(SlotCommand.class, "pack", null, null, null, "Sets the slot pack for the slot command");
    }
    @Command
    public void command(@Argument(info = "The slot pack to set the command to use") SlotPack pack){

    }
}
