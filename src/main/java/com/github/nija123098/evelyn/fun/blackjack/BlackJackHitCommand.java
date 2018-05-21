package com.github.nija123098.evelyn.fun.blackjack;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ContextType;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.economy.configs.CurrentCurrencyConfig;
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
    public static void command(MessageMaker maker, User user, @Argument(optional = true, replacement = ContextType.NONE, info = "bet amount") Integer amount) {
        BlackJackGame game = getGame(user);
        if (game == null) {
            game = new BlackJackGame(user, amount == null ? 0 : amount);
            setGame(user, game);
            maker.append(game.toString());
        } else {
            int value = game.playerHit();
            BlackJackGame finalGame = game;
            if (value > 21) {
                maker.append("__You busted__\n");
                setGame(user, null);
                ConfigHandler.changeSetting(CurrentCurrencyConfig.class, user, val -> val - finalGame.betAmount());
            } else if (game.playerBlackJack()) {
                maker.append("Black Jack\n");
                setGame(user, null);
                ConfigHandler.changeSetting(CurrentCurrencyConfig.class, user, val -> val + 2 * finalGame.betAmount());
            }
            maker.appendRaw(game.toString());
        }
    }
}
