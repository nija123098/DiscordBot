package com.github.nija123098.evelyn.command.commands;

import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.util.ExecuteShellCommand;
import com.github.nija123098.evelyn.util.HasteBin;
import com.github.nija123098.evelyn.util.PlatformDetector;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class ExcecuteCommand extends AbstractCommand {
    public ExcecuteCommand() {
        super("execute", ModuleLevel.DEVELOPMENT, null, null, "Executes stuff from the command line");
    }
    @Command
    public void command(String args, MessageMaker maker) {
        if (PlatformDetector.isWindows()) args = "cmd /c" + args;
        String out = ExecuteShellCommand.commandToExecute(args, ConfigProvider.BOT_SETTINGS.botFolder());
        if (out.length() >= 2000) {
            maker.append("Command Output:\n").appendRaw(HasteBin.post(out));
        } else maker.append("Command Output:\n```").appendRaw(out).appendRaw("```");
    }
}
