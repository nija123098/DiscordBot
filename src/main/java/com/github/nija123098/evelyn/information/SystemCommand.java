package com.github.nija123098.evelyn.information;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.launcher.Launcher;
import com.github.nija123098.evelyn.perms.BotRole;
import com.github.nija123098.evelyn.util.CareLess;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class SystemCommand extends AbstractCommand {
    public SystemCommand() {
        super("system", ModuleLevel.INFO, "sys", null, "Shows memory usage and Evelyn's version");
    }
    @Command
    public void command(MessageMaker maker) {
        maker.append("Getting memory").send();
        maker.getHeader().clear();
        maker.getTitle().clear().appendRaw("Evelyn Version: " + Launcher.EVELYN_VERSION + "\n");
        Runtime runtime = Runtime.getRuntime();
        long memoryLimit = runtime.maxMemory();
        long leastMemory = runtime.freeMemory(), freeMemory;
        for (int i = 0; i < 240; i++) {
            CareLess.lessSleep(50);
            freeMemory = runtime.freeMemory();
            if (leastMemory < freeMemory) {
                leastMemory = freeMemory;
                break;
            }
        }
        long memoryInUse = runtime.totalMemory() - leastMemory;
        maker.appendRaw((memoryInUse / memoryLimit * 100) + "% [ " + toMegabytes(memoryInUse) + " / " + toMegabytes(memoryLimit) + " ] MB");
    }

    private Long toMegabytes(long val) {
        return val / 1048576L;
    }

    @Override
    public BotRole getBotRole() {
        return BotRole.BOT_ADMIN;
    }
}
