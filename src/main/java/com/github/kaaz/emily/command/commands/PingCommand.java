package com.github.kaaz.emily.command.commands;

import com.github.kaaz.emily.command.AbstractSuperCommand;
import com.github.kaaz.emily.command.Command;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.discordobjects.helpers.MessageHelper;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 3/29/2017.
 */
public class PingCommand extends AbstractSuperCommand {
    public PingCommand() {
        super("ping", BotRole.BOT_OWNER, new String[]{}, new String[]{});
    }
    @Override
    public ModuleLevel getModule() {
        return ModuleLevel.NONE;
    }
    @Command
    public void command(MessageHelper helper){
        helper.appendTranslation("ping").send();
    }
}
