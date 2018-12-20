package com.github.nija123098.evelyn.fun.tictactoe;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.exception.ArgumentException;
import com.github.nija123098.evelyn.exception.ContextException;
import com.github.nija123098.evelyn.fun.gamestructure.AbstractGame;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class TicTacToeCommand extends AbstractCommand {
    public TicTacToeCommand() {
        super("tictactoe", ModuleLevel.FUN, "ttt, tic tac toe", null, "Plays a game of tic tac toe");
    }
    @Command
    public void command(AbstractGame game, @Argument Integer x, @Argument Integer y, User user, MessageMaker maker) {
        if (game == null) throw new ArgumentException("You don't have a active game of tic tac toe right now, do @Evelyn game start tictactoe");
        if (!(game instanceof TicTacToe)) throw new ContextException("But you are already playing a " + game.getClass().getSimpleName() + " game though!");
        TicTacToe ticTacToe = ((TicTacToe) game);
        if (x > ticTacToe.getSize() || x < 1 || y > ticTacToe.getSize() || y < 1) throw new ArgumentException("Both the x and y in a coordinate must be between 1 and " + ticTacToe.getSize() + " inclusive");
        ticTacToe.chose(user, "c" + x + "-" + y);
        maker.appendRaw(ticTacToe.getImage());
    }
}
