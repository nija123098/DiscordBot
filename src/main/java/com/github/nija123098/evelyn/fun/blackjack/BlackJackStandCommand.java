package com.github.nija123098.evelyn.fun.blackjack;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.fun.BlackJackCommand;
import com.github.nija123098.evelyn.util.ThreadHelper;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class BlackJackStandCommand extends AbstractCommand {
    private static final ScheduledExecutorService SCHEDULED_EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor(r -> ThreadHelper.getDemonThreadSingle(r, "Black-Jack-Stand-Thread"));
    public BlackJackStandCommand() {
        super(BlackJackCommand.class, "stand", "stand, stay", null, "stay", "Stands in a game of blackjack");
    }
    @Command
    public void command(MessageMaker maker, User user) {
        BlackJackGame game = BlackJackGame.getGame(user);
        if (game == null) {
            maker.append("Start a game with ").appendRaw("__bj hit__");
            return;
        }
        AtomicReference<ScheduledFuture<?>> future = new AtomicReference<>();
        future.set(SCHEDULED_EXECUTOR_SERVICE.schedule(() -> {
            int val = game.dealerHit();
            if (val > 21) {
                future.get().cancel(false);
                maker.append("The Dealer has busted\n");
            }else if (game.dealerBlackJack()) {
                future.get().cancel(false);
                maker.append("The Dealer has Black Jack\n");
            }
            if (val > 16 && !future.get().isCancelled()) {
                future.get().cancel(false);
                int pVal = game.playerValue();
                if (val > pVal) {
                    maker.append("You lose\n");
                }else if (val == pVal) {
                    maker.append("DRAW");
                }else{
                    maker.forceCompile().append("You win\n");
                }
            }
            maker.appendRaw(game.toString()).send();
        }, 1, TimeUnit.SECONDS));
        BlackJackGame.setGame(user, null);
    }
}
