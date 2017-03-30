package com.github.kaaz.emily.config;

import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Made by nija123098 on 3/30/2017.
 */
public class Playlist implements Configurable {
    private static final Map<String, Playlist> MAP = new ConcurrentHashMap<>();
    public static Playlist getPlaylist(User user, Guild guild, String args){
        String id = user + "-id-" + guild + "-id-" + args;
        return MAP.computeIfAbsent(id, s -> new Playlist(id));
    }
    public static Playlist getPlaylist(String id){
        return MAP.computeIfAbsent(id, s -> new Playlist(id));
    }
    private String id;
    private Playlist(String id) {
        this.id = id;
    }
    @Override
    public String getID() {
        return this.id;
    }

    @Override
    public ConfigLevel getConfigLevel() {
        return ConfigLevel.PLAYLIST;
    }
}
