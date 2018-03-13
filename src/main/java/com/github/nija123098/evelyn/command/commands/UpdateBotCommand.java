package com.github.nija123098.evelyn.command.commands;

import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.launcher.Launcher;
import com.github.nija123098.evelyn.util.CareLess;
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
        maker.append("Please wait while getting updates from the GIT repository").send();
        ExecuteShellCommand.commandToExecute("git -C " + ConfigProvider.UPDATE_SETTINGS.updateFolder() + " pull");
        if (ExecuteShellCommand.getOutput().contains("fatal: not a git repository"))
        {
            maker.append("Please check the settings in the config files. The currently set directory is not a valid git directory.").send();
        } else {
            if (ExecuteShellCommand.getOutput().contains("Already up-to-date")) {
                maker.append("Your local repository is already up to date.").send();
            }
            maker.append("Please wait while the update is compiled.").send();
            ExecuteShellCommand.commandToExecute("cd " + ConfigProvider.UPDATE_SETTINGS.updateFolder() + " && mvn " + ConfigProvider.UPDATE_SETTINGS.mvnArgs());
            if (ExecuteShellCommand.getOutput().contains("BUILD SUCCESS")) {
                maker.appendRaw("The project was compiled successfully. Now starting the new bot.").send();
            } else {
                maker.appendRaw("**The update was unsuccessful. Please view the build results here:**" + PastebinUtil.postToPastebin("Maven Compile Log", ExecuteShellCommand.getOutput())).send();
            }
            if (PlatformDetector.isUnix() || PlatformDetector.isWindows()) {
                ExecuteShellCommand.commandToExecute("cp " + ConfigProvider.UPDATE_SETTINGS.updateFolder() + "target/DiscordBot-1.0.0.jar " + ConfigProvider.BOT_SETTINGS.botFolder() + "Evelyn.jar");
                ExecuteShellCommand.commandToExecute(ConfigProvider.BOT_SETTINGS.startCommand());
                maker.append("The updated version of the bot has beed started. Please allow 1-2 minutes for changes to take effect.").send();
            } else if (PlatformDetector.isMac()) {
                maker.append("I am sorry, I do not know the commands needed to make this work for macOS computers. Please manually update the bot.").send();
            }
        }
    }
}