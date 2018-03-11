package com.github.nija123098.evelyn.information;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.launcher.Launcher;
import com.github.nija123098.evelyn.util.CareLess;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class SystemCommand extends AbstractCommand {
    private static AtomicInteger integer = new AtomicInteger();
    public SystemCommand() {
        super("system", ModuleLevel.INFO, "sys", null, "Shows memory usage and Evelyn's version");
    }
    @Command
    public void command(MessageMaker maker) {
        maker.append("Getting memory").send();
        maker.getHeader().clear();
        maker.mustEmbed();
        maker.getTitle().clear().appendRaw("Evelyn Version: " + Launcher.EVELYN_VERSION + "\n");
        Runtime runtime = Runtime.getRuntime();
        long memoryLimit = runtime.maxMemory();
        long leastMemory = runtime.freeMemory(), freeMemory;
        for (int i = 0; i < 6_000; i++) {
            CareLess.lessSleep(100);
            freeMemory = runtime.freeMemory();
            if (leastMemory < freeMemory) {
                leastMemory = freeMemory;
                break;
            }
        }
        long memoryInUse = runtime.totalMemory() - leastMemory;
        maker.appendRaw(getProgressbar(memoryInUse, memoryLimit) + " " + " [ " + numberInMb(memoryInUse) + " / " + numberInMb(memoryLimit) + " ]");
    }
    private String getProgressbar(long current, long max) {
        String bar = "";
        final String BLOCK_INACTIVE = "▬";
        final String BLOCK_ACTIVE = ":large_blue_circle:";
        final int BLOCK_PARTS = 12;
        int activeBLock = (int) (((float) current / (float) max) * (float) BLOCK_PARTS);
        for (int i = 0; i < BLOCK_PARTS; i++) {
            if (i == activeBLock) {
                bar += BLOCK_ACTIVE;
            } else {
                bar += BLOCK_INACTIVE;
            }
        }
        return bar;
    }

    private String numberInMb(long number) {
        return "" + (number / (1048576L)) + " mb";
    }
}
