package com.github.nija123098.evelyn.moderation.modaction;

import com.github.nija123098.evelyn.moderation.modaction.support.AbstractModAction;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;

/**
 * Made by nija123098 on 5/11/2017.
 */
public class ModActionCommand extends AbstractCommand {
    public ModActionCommand() {
        super("modaction", ModuleLevel.ADMINISTRATIVE, "ma", null, "Provides information on various disciplinary actions");
    }
    @Command
    public void command(MessageMaker maker){
        for (AbstractModAction.ModActionLevel action : AbstractModAction.ModActionLevel.values()){
            maker.appendAlternate(true, action.name() + ": ", action.getDescription() + "\n");
        }
    }
}
