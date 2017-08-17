package com.github.kaaz.emily.information;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.launcher.Launcher;
import com.github.kaaz.emily.util.Time;

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
