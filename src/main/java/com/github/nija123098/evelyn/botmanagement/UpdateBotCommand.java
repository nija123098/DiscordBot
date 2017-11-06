package com.github.nija123098.evelyn.botmanagement;

import com.github.nija123098.evelyn.BotConfig.ReadConfig;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.util.ExecuteShellCommand;
import com.github.nija123098.evelyn.util.PlatformDetector;

import java.io.IOException;

public class UpdateBotCommand extends AbstractCommand {
    public UpdateBotCommand() {
        super("updatebot", ModuleLevel.BOT_ADMINISTRATIVE, null, null, "Updates bot to latest git version. LINUX SERVER ONLY!");
    }
    @Command
    public void command(MessageMaker maker) throws IOException {
        String osType;
        if (PlatformDetector.isUnix()) {
            osType = "Windows";
            maker.append("This command can only be run whe the bot is being hosted on a Linux server. Right now it is hosted on " + osType);
        } else if (PlatformDetector.isMac()) {
            osType = "macOS";
            maker.append("This command can only be run whe the bot is being hosted on a Linux server. Right now it is hosted on " + osType);
        } else if (PlatformDetector.isUnix()) {
            maker.append("The bot will now be updated to the latest GIT version");
            ExecuteShellCommand.commandToExecute(ReadConfig.BOT_UPDATE_SCRIPT);
        }
    }
}