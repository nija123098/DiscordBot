package com.github.kaaz.emily.fun.blackjack;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.fun.BlackJackCommand;
import com.github.kaaz.emily.service.services.ScheduleService;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Made by nija123098 on 5/28/2017.
 */
public class BlackJackStandCommand extends AbstractCommand {
    public BlackJackStandCommand() {
        super(BlackJackCommand.class, "stand", "stand, stay", null, "stay", "Stands in a game of blackjack");
    }
    @Command
    public void command(MessageMaker maker, User user){
        BlackJackGame game = BlackJackGame.getGame(user);
        if (game == null){
            maker.append("Start a game with ").appendRaw("__bj hit__");
            return;
        }
        AtomicReference<ScheduleService.ScheduledTask> task = new AtomicReference<>();
        task.set(ScheduleService.scheduleRepeat(10, 1_000, () -> {
            int val = game.dealerHit();
            if (val > 21) {
                task.get().cancel();
                maker.append("The Dealer has busted\n");
            }else if (game.dealerBlackJack()) {
                task.get().cancel();
                maker.append("The Dealer has Black Jack\n");
            }
            if (val > 16 && !task.get().isCanceled()) {
                task.get().cancel();
                int pVal = game.playerValue();
                if (val > pVal){
                    maker.append("You lose\n");
                }else if (val == pVal){
                    maker.append("DRAW");
                }else{
                    maker.forceCompile().append("You win\n");
                }
            }
            maker.appendRaw(game.toString()).send();
        }));
        BlackJackGame.setGame(user, null);
    }
}
