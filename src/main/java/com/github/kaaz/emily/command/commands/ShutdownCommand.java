package com.github.kaaz.emily.command.commands;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ContextType;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.command.annotations.Argument;
import com.github.kaaz.emily.launcher.Launcher;

/**
 * Made by nija123098 on 5/11/2017.
 */
public class ShutdownCommand extends AbstractCommand {
    public ShutdownCommand() {
        super("reboot", ModuleLevel.BOT_ADMINISTRATIVE, "restart, shutdown, logout", null, "Restarts the bot");
    }
    @Command
    public void command(@Argument(optional = true, replacement = ContextType.NONE) Integer val, String remaining){
        remaining = remaining.toLowerCase();
        if (remaining.contains("now")) System.exit(val == null ? -1 : val);
        Launcher.shutdown(remaining.contains("cancel") ? null : val == null ? 0 : val, remaining.contains("prop") ? 5_000 : 30_000);
        if (!remaining.contains("soft")) {
            Thread firm = new Thread(() -> {
                try{Thread.sleep(150_000);
                } catch (InterruptedException e) {
                    System.err.println("Ended end sleep early");
                    e.printStackTrace();
                }
                Thread halt = new Thread(() -> {
                    try{Thread.sleep(150_000);
                    } catch (InterruptedException e) {
                        System.err.println("Ended halt sleep early");
                        e.printStackTrace();
                    }
                    Runtime.getRuntime().halt(val == null ? -1 : val);
                }, "Halt-Thread");
                halt.setDaemon(true);
                halt.start();
                System.exit(val == null ? -1 : val);
            }, "Firm-Thread");
            firm.setDaemon(true);
            firm.start();
        }
    }
}
