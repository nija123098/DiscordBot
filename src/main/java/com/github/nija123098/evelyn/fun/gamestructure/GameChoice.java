package com.github.nija123098.evelyn.fun.gamestructure;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class GameChoice {
    private final String name;
    private final Predicate<Team> mayChose;
    private final Consumer<Team> choice;
    public GameChoice(String name, Consumer<Team> choice, Predicate<Team> mayChose) {
        this.name = name;
        this.choice = choice;
        this.mayChose = mayChose;
    }
    public String getName() {
        return this.name;
    }
    public boolean mayChose(Team team){
        return this.mayChose.test(team);
    }
    public void chose(Team team){
        this.choice.accept(team);
    }
}
