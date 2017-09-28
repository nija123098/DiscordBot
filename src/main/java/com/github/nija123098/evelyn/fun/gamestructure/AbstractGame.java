package com.github.nija123098.evelyn.fun.gamestructure;

import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.exeption.ArgumentException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public Team getTeam(User user){
        this.ensureTeamsLoaded();
        return this.userTeamMap.get(user);
    }
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
    public List<Team> getTeams() {
        return this.teams;
    }
    public abstract String getName();
    public abstract List<GameChoice> getChoices();// must stay a list
    public abstract String getImage();
    public abstract Team getWinner();
    public abstract double[] inputNodes();
}
