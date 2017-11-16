package com.github.nija123098.evelyn.command.commands;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.CommandHandler;
import com.github.nija123098.evelyn.command.ContextType;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.information.subsription.SubscriptionLevel;
import com.github.nija123098.evelyn.launcher.Launcher;
import com.github.nija123098.evelyn.service.services.ScheduleService;
import com.github.nija123098.evelyn.template.KeyPhrase;
import com.github.nija123098.evelyn.template.Template;
import com.github.nija123098.evelyn.template.TemplateHandler;
import com.github.nija123098.evelyn.util.ExecuteShellCommand;
import com.github.nija123098.evelyn.util.PlatformDetector;

import java.io.IOException;
import java.util.Collections;

/**
 * @author Celestialdeath99
 */

public class UpdateBotCommand extends AbstractCommand {
    public UpdateBotCommand() {
        super("updatebot", ModuleLevel.DEVELOPMENT, null, null, "Updates bot to latest git version. LINUX SERVER ONLY!");
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
            maker.append("The bot will now download, compile and update itself from the latest version on GitHub.\nThis usually takes 2-3 minutes.");
            ExecuteShellCommand.commandToExecute("./Pull.sh");
            ScheduleService.schedule(17000, () -> maker.append("The GIT folder has been updated. Now compiling."));
            ScheduleService.schedule(18000, () -> ExecuteShellCommand.commandToExecute("./Build.sh"));
            ScheduleService.schedule(80000, () -> maker.append("The sources have been compiled. The bot will now restart and apply the update."));
            ScheduleService.schedule(80500, () -> ExecuteShellCommand.commandToExecute("./Update.sh"));
            ScheduleService.schedule(82000, () -> Launcher.shutdown( 1, 0, false));
        }
    }
}