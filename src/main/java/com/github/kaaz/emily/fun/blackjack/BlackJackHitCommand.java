package com.github.kaaz.emily.fun.blackjack;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.fun.BlackJackCommand;

/**
 * Made by nija123098 on 5/28/2017.
 */
public class BlackJackHitCommand extends AbstractCommand {
    public BlackJackHitCommand() {
        super(BlackJackCommand.class, "hit", "hit", null, null, "Hits and starts the game");
    }
    @Command
    public void command(MessageMaker maker, User user){
        BlackJackGame game = BlackJackGame.getGame(user);
        if (game == null) {
            game = new BlackJackGame(user);
            BlackJackGame.setGame(user, game);
            maker.append(game.toString());
        }else{
            int value = game.playerHit();
            if (value > 21) {
                maker.append("__You busted__\n");
                BlackJackGame.setGame(user, null);
            }else if (game.playerBlackJack()) {
                maker.append("Black Jack\n");
                BlackJackGame.setGame(user, null);
            }
            maker.appendRaw(game.toString());
        }
    }
}
