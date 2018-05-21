package com.github.nija123098.evelyn.fun;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ContextType;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.fun.blackjack.BlackJackGame;
import com.github.nija123098.evelyn.fun.blackjack.BlackJackHitCommand;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class BlackJackCommand extends AbstractCommand {
    public BlackJackCommand() {
        super("blackjack", ModuleLevel.FUN, "bj", null, "Play a game of blackjack!");
    }
    @Command
    public void command(MessageMaker maker, User user, @Argument(optional = true, replacement = ContextType.NONE, info = "bet amount") Integer amount) {
        BlackJackGame game = BlackJackGame.getGame(user);
        if (game == null) BlackJackHitCommand.command(maker, user, amount);
        else maker.append("You are still in a game. To finish type *blackjack stand*").appendRaw("\n" + game.toString());
    }

}
