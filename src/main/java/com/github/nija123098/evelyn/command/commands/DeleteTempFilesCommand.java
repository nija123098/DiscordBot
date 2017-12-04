package com.github.nija123098.evelyn.command.commands;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.perms.BotRole;
import com.github.nija123098.evelyn.util.FileHelper;

/**
 * @author nija123098
 * @since 1.0.0
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
