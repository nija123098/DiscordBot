package com.github.nija123098.evelyn.fun.gamestructure;

import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameHandler {
    private static final Map<Guild, Map<User, AbstractGame>> GAME_MAP = new ConcurrentHashMap<>();
    public static void register(Guild guild, AbstractGame game){
        game.getTeams().forEach(team -> team.getUsers().forEach(user -> GAME_MAP.computeIfAbsent(guild, c -> new ConcurrentHashMap<>()).put(user, game)));
    }
    public static AbstractGame getGame(Guild guild, User user){
        return GAME_MAP.getOrDefault(guild, Collections.emptyMap()).getOrDefault(user, null);
    }
    static void deregister(AbstractGame game) {
        GAME_MAP.forEach((guild, map) -> map.forEach((user, abstractGame) -> {
            if (abstractGame.equals(game)) map.remove(user);
        }));
    }
}
