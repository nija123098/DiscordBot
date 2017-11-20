package com.github.nija123098.evelyn.command.commands;

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
 * @author Celestialdeath99
 */

public class UpdateBotCommand extends AbstractCommand {
    public UpdateBotCommand() {
        super("updatebot", ModuleLevel.DEVELOPMENT, "update", null, "Updates bot to latest git version.");
    }
    @Command
    public void command(MessageMaker maker) throws IOException {
        String osType;
        if (PlatformDetector.isWindows()) {
            osType = "Windows";
            maker.append("This command can only be run whe the bot is being hosted on a Linux server not " + osType + " which it is currently on");
        } else if (PlatformDetector.isMac()) {
            osType = "macOS";
            maker.append("This command can only be run whe the bot is being hosted on a Linux server not " + osType + " which it is currently on");
        } else if (PlatformDetector.isUnix()) {
            maker.append("The bot will now download, compile and update itself from the latest version on GitHub." + "\n").mustEmbed().withColor(new Color(46, 204, 113));
            ExecuteShellCommand.commandToExecute("./Pull.sh");
            if (ExecuteShellCommand.getOutput().contains("Already up-to-date.")) {
                maker.appendRaw("\n**The bot is already at the latest version. Aborting update sequence.**");
            } else {
                maker.appendRaw("\n*GIT Pull Results:*\n" + HastebinUtil.handleHastebin(ExecuteShellCommand.getOutput()) + "\n");
                ExecuteShellCommand.commandToExecute("./Build.sh");
                if (ExecuteShellCommand.getOutput().contains("BUILD SUCCESS")) {
                    maker.appendRaw("\n*Compilation Results:*\n" + HastebinUtil.handleHastebin(ExecuteShellCommand.getOutput()) + "\n");
                    maker.append("\n**The bot will now restart to apply the updates.**").send();
                    ScheduleService.schedule(10000, () -> Launcher.shutdown(1, 0, false));
                    ExecuteShellCommand.commandToExecute("./Update.sh");
                } else
                    maker.appendRaw("\n**ERROR COMPILING BOT**. You can view the log here:\n" + HastebinUtil.handleHastebin(ExecuteShellCommand.getOutput()));
            }
        }
    }
}