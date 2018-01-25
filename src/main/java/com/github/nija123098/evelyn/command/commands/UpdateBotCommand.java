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

/**
 * @author Celestialdeath99
 * @since 1.0.0
 */
public class UpdateBotCommand extends AbstractCommand {
    public UpdateBotCommand() {
        super("updatebot", ModuleLevel.DEVELOPMENT, "update", null, "Updates bot to latest git version.");
    }
    @Command
    public void command(MessageMaker maker) {
        String commandPrefix = null;

        if (PlatformDetector.isWindows()) {
            commandPrefix = "";
        } else if (PlatformDetector.isMac()) {
            commandPrefix = "sh ";
        } else if (PlatformDetector.isUnix()) {
            commandPrefix = "./";
        }

        maker.append("The bot will now download, compile and update itself from the latest version on GitHub." + "\n").mustEmbed();
        ExecuteShellCommand.commandToExecute(commandPrefix + ConfigProvider.UPDATE_SCRIPTS.pullScript());
        if (ExecuteShellCommand.getOutput().contains("Already up-to-date.")) {
            maker.appendRaw("\n**The bot is already at the latest version. Aborting update sequence.**");
        } else {
            maker.appendRaw("\n*GIT Pull Results:*\n" + HastebinUtil.postToHastebin(ExecuteShellCommand.getOutput()) + "\n");
            ExecuteShellCommand.commandToExecute(commandPrefix + ConfigProvider.UPDATE_SCRIPTS.buildScript());
            if (ExecuteShellCommand.getOutput().contains("BUILD SUCCESS")) {
                maker.appendRaw("\n*Compilation Results:*\n" + HastebinUtil.postToHastebin(ExecuteShellCommand.getOutput()) + "\n");
                maker.append("\n**The bot will now restart to apply the updates.**").send();
                ScheduleService.schedule(10000, () -> Launcher.shutdown(1, 0, false));
                ExecuteShellCommand.commandToExecute(commandPrefix + ConfigProvider.UPDATE_SCRIPTS.updateScript());
            } else maker.appendRaw("\n**ERROR COMPILING BOT**. You can view the log here:\n" + HastebinUtil.postToHastebin(ExecuteShellCommand.getOutput()));
        }
    }
}