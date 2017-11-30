package com.github.nija123098.evelyn.command.commands;

import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.launcher.Launcher;
import com.github.nija123098.evelyn.service.services.ScheduleService;
import com.github.nija123098.evelyn.util.ExecuteShellCommand;
import com.github.nija123098.evelyn.util.HastebinUtil;
import com.github.nija123098.evelyn.util.PlatformDetector;

import java.awt.*;
import java.io.IOException;

/**
 * @Author Celestialdeath99
 * Updated on 11/28/2017
 */

public class UpdateBotCommand extends AbstractCommand {
    public UpdateBotCommand() {
        super("updatebot", ModuleLevel.DEVELOPMENT, "update", null, "Updates bot to latest git version.");
    }
    @Command
    public void command(MessageMaker maker) throws IOException {
        String commandPrefix = null;

        if (PlatformDetector.isWindows()) {
            commandPrefix = "";
        } else if (PlatformDetector.isMac()) {
            commandPrefix = "sh ";
        } else if (PlatformDetector.isUnix()) {
            commandPrefix = "./";
        }

        maker.append("The bot will now download, compile and update itself from the latest version on GitHub." + "\n").mustEmbed().withColor(new Color(46, 204, 113));
        ExecuteShellCommand.commandToExecute(commandPrefix + ConfigProvider.UPDATE_SCRIPTS.pull_script());
        if (ExecuteShellCommand.getOutput().contains("Already up-to-date.")) {
            maker.appendRaw("\n**The bot is already at the latest version. Aborting update sequence.**");
        } else {
            maker.appendRaw("\n*GIT Pull Results:*\n" + HastebinUtil.handleHastebin(ExecuteShellCommand.getOutput()) + "\n");
            ExecuteShellCommand.commandToExecute(commandPrefix + ConfigProvider.UPDATE_SCRIPTS.build_script());
            if (ExecuteShellCommand.getOutput().contains("BUILD SUCCESS")) {
                maker.appendRaw("\n*Compilation Results:*\n" + HastebinUtil.handleHastebin(ExecuteShellCommand.getOutput()) + "\n");
                maker.append("\n**The bot will now restart to apply the updates.**").send();
                ScheduleService.schedule(10000, () -> Launcher.shutdown(1, 0, false));
                ExecuteShellCommand.commandToExecute(commandPrefix + ConfigProvider.UPDATE_SCRIPTS.update_script());
            } else maker.appendRaw("\n**ERROR COMPILING BOT**. You can view the log here:\n" + HastebinUtil.handleHastebin(ExecuteShellCommand.getOutput()));
        }
    }
}