package com.github.nija123098.evelyn.command.commands;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.information.subsription.SubscriptionLevel;
import com.github.nija123098.evelyn.launcher.Launcher;
import com.github.nija123098.evelyn.template.KeyPhrase;
import com.github.nija123098.evelyn.template.Template;
import com.github.nija123098.evelyn.template.TemplateHandler;
import com.github.nija123098.evelyn.util.ExecuteShellCommand;
import com.github.nija123098.evelyn.util.PlatformDetector;

import java.io.IOException;
import java.util.Collections;

public class UpdateBotCommand extends AbstractCommand {
    public UpdateBotCommand() {
        super("updatebot", ModuleLevel.DEVELOPMENT, null, null, "Updates bot to latest git version. LINUX SERVER ONLY!");
    }
    @Command
    public void command(MessageMaker message) throws IOException {
        String osType;
        if (PlatformDetector.isWindows()) {
            osType = "Windows";
            message.append("This command can only be run whe the bot is being hosted on a Linux server not " + osType + " which it is currently on");
            message.send();
        } else if (PlatformDetector.isMac()) {
            osType = "macOS";
            message.append("This command can only be run whe the bot is being hosted on a Linux server not " + osType + " which it is currently on");
            message.send();
        } else if (PlatformDetector.isUnix()) {
            message.append("The bot will now be updated to the latest version stored in github");
            message.send();
            SubscriptionLevel.BOT_STATUS.send(new MessageMaker((Channel) null).append("I'm going down for an update. This may take a few minutes."));
            ExecuteShellCommand.commandToExecute("./Update.sh");
            SubscriptionLevel.BOT_STATUS.send(new MessageMaker((Channel) null).append("I'm going down for an update. This may take a few minutes."));
            Launcher.shutdown( -1, 0, false);
        }
    }
}