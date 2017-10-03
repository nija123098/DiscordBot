package com.github.nija123098.evelyn.fun.gamestructure.neuralnet;

import com.github.nija123098.evelyn.fun.gamestructure.AbstractGame;
import com.github.nija123098.evelyn.fun.gamestructure.GameChoice;
import com.github.nija123098.evelyn.fun.gamestructure.Team;

import java.util.List;

/**
 * The {@link AbstractGame} class to extend to add
 * {@link NeuralNet} support for the bot's playing technique.
 *
 * Otherwise the bot will make a choice at random.
 *
 * @author nija123098
 * @since 1.0.0
 */
public abstract class AbstractNeuralNetGame extends AbstractGame {
    public AbstractNeuralNetGame(List<Team> teams) {
        super(teams);
    }

    /**
     * Gets the input node values for the current situation of the game.
     *
     * @return the input node values for the current situation of the game.
     */
    public abstract double[] inputNodes();

    /**
     * Gets the number of expected input nodes.
     *
     * @return the number of expected input nodes.
     */
    public abstract int getInputWidth();

    /**
     * Gets the width in nodes of the layers of a hidden nodes.
     *
     * @return the width in nodes of the layers of a hidden nodes.
     */
    public abstract int getHiddenLayerWidth();

    /**
     * Gets the number of layers of nodes for the hidden nodes.
     *
     * @return the number of layers of nodes for the hidden nodes.
     */
    public abstract int getHiddenLayerCount();

    /**
     * Gets the number of expected output nodes.
     *
     * @return the number of expected output nodes.
     */
    public abstract int getOutputWidth();

    /**
     * Gets {@link GameChoice} decision should be made based on the output of the {@link NeuralNet}.
     *
     * @param output the output of the {@link NeuralNet} to make a decision on.
     * @return what {@link GameChoice} should be made based on the output of the {@link NeuralNet}.
     */
    public abstract GameChoice getDecision(double[] output);
}
