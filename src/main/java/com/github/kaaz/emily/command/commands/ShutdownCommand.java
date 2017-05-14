package com.github.kaaz.emily.command.commands;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ContextType;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.command.anotations.Argument;
import com.github.kaaz.emily.launcher.Launcher;

/**
 * Made by nija123098 on 5/11/2017.
 */
public class ShutdownCommand extends AbstractCommand {
    public ShutdownCommand() {
        super("reboot", ModuleLevel.BOT_ADMINISTRATIVE, "restart, shutdown", null, "Restarts the bot");
    }
    @Command
    public void command(@Argument(optional = true, replacement = ContextType.NONE) Integer val, String remaining){
        Launcher.shutdown(remaining.contains("cancel") ? null : val == null ? 0 : val);
        if (remaining.contains("firm")) new Thread(() -> {
            try{Thread.sleep(300_000);
            } catch (InterruptedException e) {
                System.err.println("Ended halt sleep early");
                e.printStackTrace();
            }
            Runtime.getRuntime().halt(val == null ? -1 : val);
        }, "Halt-Thread").start();
    }
}
