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
import com.github.nija123098.evelyn.util.HastebinUtil;
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
            maker.append("The bot will now download, compile and update itself from the latest version on GitHub." + "\n" + "This usually takes 2-3 minutes.");
            ExecuteShellCommand.commandToExecute("./Pull.sh");
            maker.appendRaw("\nGIT Pull Results:\n```" + ExecuteShellCommand.getOutput() + "```");
            ExecuteShellCommand.commandToExecute("./Build.sh");
            if (ExecuteShellCommand.getOutput().length() < 2000) {
                maker.appendRaw("\nCompilation Results:\n```" + ExecuteShellCommand.getOutput() + "```");
            } else maker.appendRaw("\nCompilation Results:\n" + HastebinUtil.handleHastebin(ExecuteShellCommand.getOutput()));
            maker.append("\nThe bot will now restart to apply the updates.").send();
            ScheduleService.schedule(5000, () -> Launcher.shutdown(1,0,false));
            ExecuteShellCommand.commandToExecute("./Update.sh");
        }
    }
}