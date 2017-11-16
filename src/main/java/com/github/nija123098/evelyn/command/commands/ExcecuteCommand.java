package com.github.nija123098.evelyn.command.commands;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.exeption.DevelopmentException;
import com.github.nija123098.evelyn.util.ExecuteShellCommand;
import com.github.nija123098.evelyn.util.PlatformDetector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

/**
 * Made by nija123098 on 8/8/2017.
 */
public class ExcecuteCommand extends AbstractCommand {
    public ExcecuteCommand() {
        super("execute", ModuleLevel.DEVELOPMENT, null, null, "Executes stuff from the command line");
    }
    @Command
    public void command(String args, MessageMaker maker){
        if (PlatformDetector.isWindows()) args = "cmd /c" + args;
        ExecuteShellCommand.commandToExecute(args);
        maker.append("Command Output:\n```").appendRaw(ExecuteShellCommand.getOutput()).append("```");
    }
}
