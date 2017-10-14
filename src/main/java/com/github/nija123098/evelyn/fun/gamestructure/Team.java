package com.github.nija123098.evelyn.fun.gamestructure;

import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient;
import com.github.nija123098.evelyn.discordobjects.wrappers.Role;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.exeption.ArgumentException;
import com.github.nija123098.evelyn.exeption.ContextException;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A type for organizing a single user or members with a role's
 * actions in order to play a instance of {@link AbstractGame}.
 */
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

    /**
     * The constructor for a solo team.
     *
     * @param user the user to go solo.
     */
    public Team(User user){
        this(1F, user, null);
    }

    /**
     * The constructor for a {@link Role} based team.
     *
     * @param voteRequirement the value 1 through 0 to require voting for.
     *                        The majority wins once enough users have voted.
     * @param role the {@link Role} to track all users as a member of the team.
     */
    public Team(Float voteRequirement, Role role){
        this(voteRequirement, null, role);
        if (role.getUsers().isEmpty()) throw new ContextException("There must be people on a team though");
    }
    public void load(AbstractGame game){
        this.choiceMap = game.getChoices().stream().collect(Collectors.toMap(GameChoice::getName, Function.identity()));
    }
    Float chose(User user, String choice){// don't use this to make decisions in command aliasing
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

    /**
     * Gets the users registered tho the playing team.
     *
     * @return the users registered tho the playing team.
     */
    public List<User> getUsers() {
        return this.user == null ? this.role.getUsers() : Collections.singletonList(this.user);
    }

    /**
     * Gets the string representation of a mention to the team.
     *
     * @return the string representation of a mention to the team.
     */
    public String mention(){
        return this.user == null ? this.role.mention() : this.user.mention();
    }

    public Float getVoteRequirement() {
        return this.voteRequirement;
    }

    /**
     * Gets if the bot is the only team member on the instance team.
     *
     * @return if the bot is the only team member on the instance team.
     */
    public boolean isOurTeam(){
        return DiscordClient.getOurUser().equals(this.user);
    }
}
