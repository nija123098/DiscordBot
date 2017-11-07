package com.github.nija123098.evelyn.command.commands;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.exeption.DevelopmentException;

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
        try {
            if (System.getProperty("os.name").startsWith("Windows")) args = "cmd /c " + args;
            Process process = Runtime.getRuntime().exec(args);
            process.waitFor(1, TimeUnit.MINUTES);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) sb.append(line).append("\n");
            maker.append("Command output:\n").appendRaw(sb.toString());
        } catch (InterruptedException | IOException e) {
            throw new DevelopmentException("Attempted to execute a command but failed: " + args, e);
        }
    }
}
