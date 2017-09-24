package com.github.nija123098.evelyn.fun.gamestructure;

import com.github.nija123098.evelyn.discordobjects.wrappers.Role;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.exeption.ArgumentException;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class Team {
    private final Float voteRequirement;
    private final User user;
    private final Role role;
    private transient Map<User, String> voteMap;
    private transient Map<String, GameChoice> choiceMap;
    private Team(Float voteRequirement, User user, Role role) {
        voteRequirement = voteRequirement / 100;
        if (voteRequirement < 0 || voteRequirement > 1) throw new ArgumentException("Please chose a vote requirement between 0 and 100");
        this.voteRequirement = voteRequirement;
        this.user = user;
        this.role = role;
        if (this.role == null) voteMap = null;
        else this.voteMap = new HashMap<>();
    }
    public Team(User user){
        this(1F, user, null);
    }
    public Team(Float voteRequirement, Role role){
        this(voteRequirement, null, role);
    }
    public void load(AbstractGame game){
        List<GameChoice> choices = game.getChoices();
        this.choiceMap = new HashMap<>(choices.size() + 2, 1);
        choices.forEach(gameChoice -> this.choiceMap.put(gameChoice.getName(), gameChoice));
    }
    Float chose(User user, String choice){
        if (!this.choiceMap.containsKey(choice)) throw new ArgumentException("Invalid move: " + choice);
        if (this.voteMap != null){
            this.voteMap.put(user, choice);
            Map<String, Integer> map = new HashMap<>();
            this.voteMap.forEach((u, s) -> map.compute(s, (s1, integer) -> integer == null ? 1 : ++integer));
            AtomicInteger integer = new AtomicInteger();
            Set<String> choices = new HashSet<>();
            map.forEach((s, votes) -> {
                if (integer.get() == votes) choices.add(s);
                else if (integer.get() < votes){
                    integer.set(votes);
                    choices.clear();
                }
            });
            float percentVoted = this.voteMap.size() / (float) this.role.getUsers().size();
            if (choices.size() > 1 || percentVoted < this.voteRequirement) return percentVoted;
            this.voteMap.clear();
        }
        GameChoice gameChoice = this.choiceMap.get(choice);
        if (!gameChoice.mayChose(this)) throw new ArgumentException("You can't chose that!");
        gameChoice.chose(this);
        return null;
    }
    public List<User> getUsers() {
        return this.user == null ? this.role.getUsers() : Collections.singletonList(this.user);
    }
    public String mention(){
        return this.user == null ? this.role.mention() : this.user.mention();
    }
    public Float getVoteRequirement() {
        return this.voteRequirement;
    }
}
