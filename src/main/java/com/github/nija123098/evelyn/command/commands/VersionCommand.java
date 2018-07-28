package com.github.nija123098.evelyn.command.commands;

import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.command.configs.LastBotUpdaterUseConfig;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.GlobalConfigurable;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.util.ExecuteShellCommand;

import java.awt.*;

/**
 * @author Celestialdeath99
 * @since 1.0.0
 */
public class VersionCommand extends AbstractCommand {
    public VersionCommand() {
        super("version", ModuleLevel.DEVELOPMENT, null, null, "Displays the current commit the bot is running.");
    }
    @Command
    public void command(MessageMaker maker) {
        if (ConfigProvider.BOT_SETTINGS.isRunningInContainer()) {
            maker.withColor(new Color(175, 30,5));
            maker.getTitle().clear().appendRaw("\uD83D\uDEE0 Bot Version \uD83D\uDEE0");
            maker.appendRaw("This command will not work when Evelyn is running in a container. Sorry for the inconvenience.");
        } else {
            String out = ExecuteShellCommand.commandToExecute("git rev-parse --short HEAD", ConfigProvider.UPDATE_SETTINGS.updateFolder());
            maker.withColor(new Color(175, 30,5));
            maker.getTitle().clear().appendRaw("\uD83D\uDEE0 Bot Version \uD83D\uDEE0");
            maker.getNote().clear().appendRaw("Last Update");
            maker.withTimestamp(ConfigHandler.getSetting(LastBotUpdaterUseConfig.class, GlobalConfigurable.GLOBAL));
            maker.appendRaw("**" + out + "**");
        }
    }
}