package com.github.kaaz.emily.information;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;

/**
 * @author Soarnir
 */
public class PingFancyCommand extends AbstractCommand {
    private static final String[] pingMessages = new String[]{
            ":ping_pong::white_small_square::black_small_square::black_small_square::ping_pong:",
            ":ping_pong::black_small_square::white_small_square::black_small_square::ping_pong:",
            ":ping_pong::black_small_square::black_small_square::white_small_square::ping_pong:",
            ":ping_pong::black_small_square::white_small_square::black_small_square::ping_pong:",
            ":ping_pong::white_small_square::black_small_square::black_small_square::ping_pong:"
    };
    public PingFancyCommand() {
        super(PingCommand.class, "fancy", null, null, null, "A fancy version of the ping command");
    }
    @Command
    public void command(MessageMaker maker){
        int lastResult;
        int sum = 0, min = 999, max = 0;
        long start = System.currentTimeMillis();
        for (int j = 0; j <5; j++) {
            maker.appendRaw(pingMessages[j]).send();
            maker.forceCompile().getHeader().clear();
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
        maker.appendRaw("Average ping is: " + (int)Math.ceil(sum/5f) + "ms (min: " + min + "ms, max: " + max + "ms)");
    }
}
