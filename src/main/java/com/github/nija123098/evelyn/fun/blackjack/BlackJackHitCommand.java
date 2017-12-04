package com.github.nija123098.evelyn.fun.blackjack;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.fun.BlackJackCommand;

import static com.github.nija123098.evelyn.fun.blackjack.BlackJackGame.getGame;
import static com.github.nija123098.evelyn.fun.blackjack.BlackJackGame.setGame;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class BlackJackHitCommand extends AbstractCommand {
    public BlackJackHitCommand() {
        super(BlackJackCommand.class, "hit", "hit", null, null, "Hits and starts the game");
    }

    @Command
    public void command(MessageMaker maker, User user) {
        BlackJackGame game = getGame(user);
        if (game == null) {
            game = new BlackJackGame(user);
            setGame(user, game);
            maker.append(game.toString());
        } else {
            int value = game.playerHit();
            if (value > 21) {
                maker.append("__You busted__\n");
                setGame(user, null);
            } else if (game.playerBlackJack()) {
                maker.append("Black Jack\n");
                setGame(user, null);
            }
            maker.appendRaw(game.toString());
        }
    }
}
