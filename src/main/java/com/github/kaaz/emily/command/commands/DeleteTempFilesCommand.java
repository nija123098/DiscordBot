package com.github.kaaz.emily.command.commands;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.perms.BotRole;
import com.github.kaaz.emily.util.FileHelper;

/**
 * Made by nija123098 on 6/25/2017.
 */
public class DeleteTempFilesCommand extends AbstractCommand {
    public DeleteTempFilesCommand() {
        super("deletetempfiles", BotRole.BOT_ADMIN, ModuleLevel.DEVELOPMENT, "dtf", null, "Clears temp files");
    }
    @Command
    public void command(){
        FileHelper.clearTemps();
    }
}
