package com.github.nija123098.evelyn.fun.gamestructure.neuralnet;

import com.github.nija123098.evelyn.fun.gamestructure.AbstractGame;
import com.github.nija123098.evelyn.fun.gamestructure.GameChoice;
import com.github.nija123098.evelyn.fun.gamestructure.Team;

import java.util.List;

public abstract class AbstractNeuralNetGame extends AbstractGame {
    public AbstractNeuralNetGame(List<Team> teams) {
        super(teams);
    }
    public abstract double[] inputNodes();
    public abstract int getInputCount();
    public abstract int getHiddenLayerWidth();
    public abstract int getHiddenLayerCount();
    public abstract int getOutputCount();
    public abstract GameChoice getDecision(double[] output);
}
