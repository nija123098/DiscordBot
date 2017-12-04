package com.github.nija123098.evelyn.fun.gamestructure;

import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.exception.BotException;
import com.github.nija123098.evelyn.fun.gamestructure.neuralnet.AbstractNeuralNetGame;

import java.awt.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * An exception which indicates a winner and travels
 * up the stack to leave a message for the teams.
 *
 * An exception is used to ensure no further processing
 * is done on the game since such might change the outcome.
 *
 * @author nija123098
 * @since 1.0.0
 */
public class GameResultException extends BotException {
    private Team winner;
    private AbstractGame game;

    /**
     * Makes an exception which reports a winner.
     *
     * @param game the game to register results for.
     * @param winner the winner, or null if stalemate.
     */
    public GameResultException(AbstractGame game, Team winner) {
        this.game = game;
        this.winner = winner;
        GameHandler.deregister(game);
        if (winner != null && winner.isOurTeam()) GamePlayHandler.reportWin((AbstractNeuralNetGame) game);
    }
    @Override
    public MessageMaker makeMessage(Channel channel) {
        AtomicReference<String> reference = new AtomicReference<>("");
        this.game.getTeams().forEach(team -> reference.updateAndGet(s -> s + " " + team.mention()));
        return new MessageMaker(channel).withColor(Color.CYAN).getExternal().appendRaw(reference.get()).getMaker().append(winner == null ? "You all lost!" : this.winner.mention() + " won!").appendRaw("\n\n" + this.game.getImage()).getTitle().append("Game result!").getMaker();
    }
}
