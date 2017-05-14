package com.github.kaaz.emily.automoderation.modaction;

import com.github.kaaz.emily.automoderation.modaction.support.AbstractModAction;
import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 5/11/2017.
 */
public class ModActionCommand extends AbstractCommand {
    public ModActionCommand() {
        super("modaction", BotRole.GUILD_TRUSTEE, ModuleLevel.ADMINISTRATIVE, "ma", null, "Provides information on various disciplinary actions");
    }
    @Command
    public void command(MessageMaker maker){
        for (AbstractModAction.ModActionLevel action : AbstractModAction.ModActionLevel.values()){
            maker.appendAlternate(true, action.name() + ": ", action.getDescription() + "\n");
        }
    }
}
