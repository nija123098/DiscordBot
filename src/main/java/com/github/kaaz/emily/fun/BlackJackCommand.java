package com.github.kaaz.emily.fun;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.fun.blackjack.BlackJackGame;

/**
 * Made by nija123098 on 5/28/2017.
 */
public class BlackJackCommand extends AbstractCommand {
    public BlackJackCommand() {
        super("blackjack", ModuleLevel.FUN, "bj", null, "Play a game of blackjack!");
    }
    @Command
    public void command(MessageMaker maker, User user){
        BlackJackGame game = BlackJackGame.getGame(user);
        if (game == null) maker.append("You are not playing a game, to start use !blackjack hit");
        else maker.append("You are still in a game. To finish type *blackjack stand*").appendRaw("\n" + game.toString());
    }
}
