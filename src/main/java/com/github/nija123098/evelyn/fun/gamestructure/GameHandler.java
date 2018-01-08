package com.github.nija123098.evelyn.fun.gamestructure;

import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.exeption.ContextException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Handles tracking for existing games.
 *
 * @author nija123098
 * @since 1.0.0
 */
public class GameHandler {
    private static final Map<Guild, Map<User, AbstractGame>> GAME_MAP = new ConcurrentHashMap<>();
    public static void register(Guild guild, AbstractGame game){
        game.getTeams().forEach(team -> team.getUsers().forEach(user -> GAME_MAP.computeIfAbsent(guild, c -> new ConcurrentHashMap<>()).put(user, game)));
    }
    public static AbstractGame getGame(Guild guild, User user){
        AbstractGame game = GAME_MAP.getOrDefault(guild, Collections.emptyMap()).get(user);
        if (game == null) throw new ContextException("You are not currently playing a game, to start do @Evelyn game start <gamename>");
        return game;
    }
    static void deregister(AbstractGame game) {
        GAME_MAP.forEach((guild, map) -> map.forEach((user, abstractGame) -> {
            if (abstractGame.equals(game)) map.remove(user);
        }));
    }
}
