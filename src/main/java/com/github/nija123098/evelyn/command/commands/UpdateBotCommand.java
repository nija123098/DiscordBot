package com.github.nija123098.evelyn.command.commands;

import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.util.ExecuteShellCommand;
import com.github.nija123098.evelyn.util.PastebinUtil;
import com.github.nija123098.evelyn.util.PlatformDetector;

/**
 * @author Celestialdeath99
 * @since 1.0.0
 */
public class UpdateBotCommand extends AbstractCommand {
    public UpdateBotCommand() {
        super("updatebot", ModuleLevel.DEVELOPMENT, "update, upgrade", null, "Updates bot to latest git version.");
    }
    @Command
    public void command(MessageMaker maker) {

        ExecuteShellCommand.commandToExecute("git pull", ConfigProvider.UPDATE_SETTINGS.updateFolder());
        if (ExecuteShellCommand.getOutput().contains("Already up-to-date")) {
            maker.appendRaw("The bot is already up to date\n. *Aborting the update process*");
        } else {
            ExecuteShellCommand.commandToExecute("mvn " + ConfigProvider.UPDATE_SETTINGS.mvnArgs(), ConfigProvider.UPDATE_SETTINGS.updateFolder());
            if (ExecuteShellCommand.getOutput().contains("BUILD FAILURE")) {
                maker.appendRaw("**The update was unsuccessful. Please view the build results here:**\n" + PastebinUtil.postToPastebin("Maven Compile Log", ExecuteShellCommand.getOutput()));
            } else {
                if (PlatformDetector.isUnix() || PlatformDetector.isWindows()) {
                    ExecuteShellCommand.commandToExecute("cp " + ConfigProvider.UPDATE_SETTINGS.updateFolder() + "target/DiscordBot-1.0.0.jar " + ConfigProvider.BOT_SETTINGS.botFolder() + "Evelyn.jar", ConfigProvider.BOT_SETTINGS.botFolder());
                    maker.append("The bot has been updated. Please allow 1-2 minutes for changes to take effect.\n");
                    ExecuteShellCommand.commandToExecute(ConfigProvider.BOT_SETTINGS.startCommand(), ConfigProvider.BOT_SETTINGS.botFolder());
                } else if (PlatformDetector.isMac()) {
                    maker.append("I am sorry, I do not know the commands needed to make this work for macOS computers. Please manually update the bot.");
                }
            }
        }
    }
}