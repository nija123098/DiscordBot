package com.github.nija123098.evelyn.fun.tictactoe;

import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient;
import com.github.nija123098.evelyn.fun.gamestructure.GameChoice;
import com.github.nija123098.evelyn.fun.gamestructure.GameResultException;
import com.github.nija123098.evelyn.fun.gamestructure.Team;
import com.github.nija123098.evelyn.fun.gamestructure.neuralnet.AbstractNeuralNetGame;
import com.github.nija123098.evelyn.util.ArrayUtils;
import com.github.nija123098.evelyn.util.EmoticonHelper;
import com.github.nija123098.evelyn.util.LanguageHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TicTacToe extends AbstractNeuralNetGame {
    private final int size;
    private Boolean[][] grid;
    private int turn;
    private boolean noCheck;
    public TicTacToe(Team red, Team blue) {
        this(red, blue, 3);
    }
    public TicTacToe(Team red, Team blue, int size) {
        super(Arrays.asList(red, blue));
        this.size = size;
        this.grid = new Boolean[this.size][this.size];
    }
    @Override
    public String getName() {
        return "tictactoe";
    }
    public int getSize() {
        return this.size;
    }
    @Override
    public List<GameChoice> getChoices() {
        List<GameChoice> choices = new ArrayList<>(this.size * this.size);
        AtomicInteger i = new AtomicInteger(), j = new AtomicInteger();
        for (; i.get() < this.size; i.incrementAndGet()) {
            j.set(0);
            for (; j.get() < this.size; j.incrementAndGet()) {
                int x = i.get(), y = j.get();
                choices.add(new GameChoice("c" + (i.get() + 1) + "-" + (j.get() + 1), team -> this.grid[x][y] = this.getTeams().get(0).equals(team), team -> this.grid[x][y] == null));
            }
        }
        return choices;
    }
    @Override
    public String getImage() {
        StringBuilder s = new StringBuilder(EmoticonHelper.getChars("small_orange_diamond", true) + EmoticonHelper.getChars("one", true) + EmoticonHelper.getChars("two", true) + EmoticonHelper.getChars("three", true) + "\n");
        for (int i = 0; i < this.size; i++) {
            s.append(EmoticonHelper.getChars(LanguageHelper.getInteger(i + 1), true));
            for (int j = 0; j < this.size; j++) {
                if (this.grid[i][j] == null) s.append(EmoticonHelper.getChars("black_large_square", true));
                else s.append(EmoticonHelper.getChars(this.grid[i][j] ? "x" : "o", true));
            }
            if (i != this.size-1) s.append("\n");
        }
        return s.toString();
    }
    @Override
    public Team getWinner() {
        if (++this.turn > 4 && !this.noCheck){
            this.noCheck = true;
            Boolean[][] originalGrid = this.grid;
            this.grid = ArrayUtils.fillNull(ArrayUtils.copy(originalGrid), true);
            if (getWinner() == null){
                this.grid = ArrayUtils.fillNull(ArrayUtils.copy(originalGrid), false);
                if (this.getWinner() == null) {
                    this.grid = originalGrid;
                    throw new GameResultException(this, null);
                }
            }
            this.grid = originalGrid;
            this.noCheck = false;
        }
        if (this.turn < 3) return null;
        Boolean side;
        boolean fail;
        for (int i = 0; i < this.size; i++) {
            side = this.grid[i][0];
            fail = false;
            for (int j = 1; j < this.size; j++) {
                if (side == null || side != this.grid[i][j]) {
                    fail = true;
                    break;
                }
            }
            if (!fail) return side ? this.getTeams().get(0) : this.getTeams().get(1);
        }
        for (int i = 0; i < this.size; i++) {
            side = this.grid[0][i];
            fail = false;
            for (int j = 1; j < this.size; j++) {
                if (side == null || side != this.grid[j][i]) {
                    fail = true;
                    break;
                }
            }
            if (!fail) return side ? this.getTeams().get(0) : this.getTeams().get(1);
        }
        side = this.grid[0][0];
        fail = false;
        for (int i = 1; i < this.size; i++) {
            if (this.grid[i][i] == null || this.grid[i][i] != side){
                fail = true;
                break;
            }
        }
        if (!fail) return side ? this.getTeams().get(0) : this.getTeams().get(1);
        side = this.grid[0][this.size - 1];
        fail = false;
        for (int i = 1; i < this.size; ++i) {
            if (this.grid[i][this.size - i - 1] == null || this.grid[i][this.size - i - 1] != side){
                fail = true;
                break;
            }
        }
        if (!fail) return side ? this.getTeams().get(0) : this.getTeams().get(1);
        return null;
    }
    @Override
    public double[] inputNodes() {
        int index = 0;
        double[] doubles = new double[3 * this.size * this.size];
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                if (this.grid[i][j] == null) doubles[index] = 1;
                else if (this.grid[i][j]) doubles[index + 1] = 1;
                else doubles[index + 2] = 1;
                index += 3;
            }
        }
        return doubles;
    }
    @Override
    public int getInputWidth() {
        return 27;
    }
    @Override
    public int getHiddenLayerWidth() {
        return 27;
    }
    @Override
    public int getHiddenLayerCount() {
        return 2;
    }
    @Override
    public int getOutputWidth() {
        return 9;
    }
    @Override
    public GameChoice getDecision(double[] output) {
        double highestWeight = -1;
        int choice = -1;
        List<GameChoice> choices = this.getChoices();
        for (int i = 0; i < output.length; i++) {
            if (choices.get(i).mayChose(this.getTeam(DiscordClient.getOurUser())) && highestWeight < output[i]) {
                choice = i;
                highestWeight = output[i];
            }
        }
        if (choice == -1) throw new GameResultException(this, null);
        return choices.get(choice);
    }
}
