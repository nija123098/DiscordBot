package com.github.nija123098.evelyn.information;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.launcher.Launcher;
import com.github.nija123098.evelyn.util.Time;

public class UptimeCommand extends AbstractCommand {
    private static Long launchTime;
    public UptimeCommand() {
        super("uptime", ModuleLevel.INFO, null, null, "Shows how long the bot has been online");
        Launcher.registerStartup(() -> launchTime = System.currentTimeMillis());
    }
    @Command
    public void command(MessageMaker maker){
        maker.append("I have been online for " + Time.getAbbreviated(System.currentTimeMillis() - launchTime));
    }
}
