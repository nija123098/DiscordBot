package com.github.nija123098.evelyn.fun.gamestructure;

import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.exeption.ArgumentException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The superclass for game instances which support {@link Team}s.
 *
 * @author nija123098
 * @since 1.0.0
 */
public abstract class AbstractGame {
    private int teamTurn;
    private List<Team> teams;
    private transient Map<User, Team> userTeamMap;
    public AbstractGame(List<Team> teams){
        this.teams = teams;
    }
    private void ensureTeamsLoaded(){
        if (this.userTeamMap != null) return;
        this.userTeamMap = new HashMap<>();
        this.teams.forEach(team -> {
            team.load(this);
            team.getUsers().forEach(user -> this.userTeamMap.put(user, team));
        });
    }

    /**
     * Gets the {@link Team} a user belongs to.
     *
     * @param user the user to get the team for.
     * @return the {@link Team} a user belongs to.
     */
    protected Team getTeam(User user){
        this.ensureTeamsLoaded();
        return this.userTeamMap.get(user);
    }

    /**
     * Registers the given {@link User}'s choice.
     *
     * @param user the user who is chosing a {@link GameChoice}.
     * @param s the name of the {@link GameChoice} to chose.
     * @return the value of the team not voted 0 through 1.
     */
    public Float chose(User user, String s){
        Team team = this.getTeam(user);
        if (this.teams.indexOf(team) != this.teamTurn) throw new ArgumentException("It isn't your turn yet");
        Float f = team.chose(user, s);
        if (f != null && f < team.getVoteRequirement()) return f;
        Team winner = this.getWinner();
        if (winner != null) throw new GameResultException(this, winner);
        ++this.teamTurn;
        this.teamTurn %= this.teams.size();
        if (this.teams.get(teamTurn).isOurTeam()) GamePlayHandler.decideGame(this);
        return null;
    }

    /**
     * Gets a list of the {@link Team}s competing in the game.
     *
     * @return a list of the {@link Team}s competing in the game.
     */
    public List<Team> getTeams() {
        return this.teams;
    }

    /**
     * Gets the name of the game without spaces or special characters.
     *
     * @return the name of the game without spaces or special characters.
     */
    public abstract String getName();

    /**
     * A list of all choices a {@link Team} could make at any
     * point in the game, regardless of whether or not at the
     * time the method is called it is allowed to be picked.
     *
     * @return a list of choices.
     */
    public abstract List<GameChoice> getChoices();// must stay a list

    /**
     * Gets a representation of the game at the moment of calling.
     *
     * @return a representation of the game at the moment of calling.
     */
    public abstract String getImage();

    /**
     * Gets the winner at the time of calling,
     * or null if there is no winner yet.
     *
     * @return the winner at the time of calling,
     * or null if there is no winner yet.
     */
    public abstract Team getWinner();
}
