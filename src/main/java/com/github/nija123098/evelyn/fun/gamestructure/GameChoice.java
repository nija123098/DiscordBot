package com.github.nija123098.evelyn.fun.gamestructure;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * A representation of a single option a user may chose at any point.
 *
 * @author nija123098
 * @since 1.0.0
 */
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

    /**
     * Gets if the given {@link Team} has the option
     * currently to chose the choice instance as an action.
     *
     * @param team the team to get the possibility of choice for.
     * @return if the team is allowed to chose this option.
     */
    public boolean mayChose(Team team){
        return this.mayChose.test(team);
    }

    /**
     * Chooses and preforms the choice without check.
     *
     * @param team the team to preform the choice.
     */
    void chose(Team team){
        this.choice.accept(team);
    }
}
