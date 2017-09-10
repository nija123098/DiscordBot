package com.github.nija123098.evelyn.information;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.launcher.Reference;
import com.github.nija123098.evelyn.util.Care;

/**
 * Made by nija123098 on 5/11/2017.
 */
public class SystemCommand extends AbstractCommand {
    public SystemCommand() {
        super("system", ModuleLevel.INFO, "sys", null, "Shows memory usage and Emily's version");
    }
    @Command
    public void command(MessageMaker maker){
        maker.append("Emily Version: " + Reference.EMILY_VERSION + "\n");
        Runtime runtime = Runtime.getRuntime();
        long memoryLimit = runtime.maxMemory();
        long leastMemory = runtime.freeMemory(), freeMemory;
        for (int i = 0; i < 120_000; i++) {
            Care.lessSleep(1);
            freeMemory = runtime.freeMemory();
            if (leastMemory < freeMemory) break;
        }
        long memoryInUse = runtime.totalMemory() - runtime.freeMemory();
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
