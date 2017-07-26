package com.github.kaaz.emily.information;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Argument;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.util.EmoticonHelper;

/**
 * Made by nija123098 on 3/30/2017.
 */
public class PingCommand extends AbstractCommand {
    public PingCommand() {
        super("ping", ModuleLevel.INFO, null, "ping_pong", "Checks if the bot is responding");
    }

    private static final String[] pingMessages = new String[]{
            ":ping_pong::white_small_square::black_small_square::black_small_square::ping_pong:",
            ":ping_pong::black_small_square::white_small_square::black_small_square::ping_pong:",
            ":ping_pong::black_small_square::black_small_square::white_small_square::ping_pong:",
            ":ping_pong::black_small_square::white_small_square::black_small_square::ping_pong:",
            ":ping_pong::white_small_square::black_small_square::black_small_square::ping_pong:"
    };

    @Command
    public void command(@Argument(optional = true) String argument, MessageMaker helper){
        if (argument.contains("fancy")) {
            int lastResult;
            int sum = 0, min = 999, max = 0;
            long start = System.currentTimeMillis();
            for (int j = 0; j <5; j++) {
                helper.appendRaw(pingMessages[j]).send();
                helper.forceCompile().getHeader().clear();
                lastResult = (int) (System.currentTimeMillis() - start);
                sum += lastResult;
                min = Math.min(min, lastResult);
                max = Math.max(max, lastResult);
                try {
                    Thread.sleep(1_100L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                start = System.currentTimeMillis();
            }
            helper.appendRaw("Average ping is: " + (int)Math.ceil(sum/5f) + "ms (min: " + min + "ms, max: " + max + "ms)");
        } else {
            long time = System.currentTimeMillis();
            helper.appendRaw(EmoticonHelper.getChars("outbox_tray", false) + " ").append("Checking ping").send();
            helper.forceCompile().getHeader().clear();
            helper.appendRaw(EmoticonHelper.getChars("inbox_tray", false) + " ").append("ping is " + (System.currentTimeMillis() - time) + "ms");
        }
    }
}
