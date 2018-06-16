package com.github.nija123098.evelyn.information;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.Configurable;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.util.CareLess;

/**
 * @author Soarnir
 * @since 1.0.0
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
    public void command(MessageMaker maker) {
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
            CareLess.lessSleep(1_100L);
            start = System.currentTimeMillis();
        }
        maker.appendRaw("Average ping is: " + (int)Math.ceil(sum/5f) + "ms (min: " + min + "ms, max: " + max + "ms)");
    }

    @Override
    public long getCoolDown(Class<? extends Configurable> clazz) {
        return clazz.equals(User.class) ? 60_000 : super.getCoolDown(clazz);
    }
}
