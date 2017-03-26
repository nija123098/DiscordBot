package com.github.kaaz.emily.discordobjects.wrappers;

import sx.blah.discord.api.IShard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * Made by nija123098 on 2/27/2017.
 */
public class Shard {
    private static final Map<String, Shard> MAP = new ConcurrentHashMap<>();
    public static Shard getShard(String id){
        return MAP.values().stream().filter(shard -> shard.shardID() == Integer.parseInt(id)).collect(Collectors.toList()).get(0);
    }
    static Shard getShard(IShard shard){
        return MAP.computeIfAbsent(shard.getInfo()[0] + "", s -> new Shard(shard));
    }
    static List<Shard> getShards(List<IShard> iShards){
        List<Shard> shards = new ArrayList<>(iShards.size());
        iShards.forEach(iShard -> shards.add(getShard(iShard)));
        return shards;
    }
    private final AtomicReference<IShard> shard;
    Shard(IShard shard) {
        this.shard = new AtomicReference<>(shard);
    }
    IShard shard(){
        return this.shard.get();
    }

    //WRAPPER METHODS
    public int shardID(){
        return shard().getInfo()[0];
    }

    public boolean isLoggedIn() {
        return shard().isLoggedIn();
    }

    public long getResponseTime() {
        return shard().getResponseTime();
    }

    public void changePlayingText(String playingText) {
        shard().changePlayingText(playingText);
    }

    public void online(String playingText) {
        shard().online(playingText);
    }

    public void online() {
        shard().online();
    }

    public void idle(String playingText) {
        shard().idle(playingText);
    }

    public void idle() {
        shard().idle();
    }

    public void streaming(String playingText, String streamingUrl) {
        shard().streaming(playingText, streamingUrl);
    }

    public List<Channel> getChannels(boolean includePrivate) {
        return Channel.getChannels(shard().getChannels(includePrivate));
    }

    public List<Channel> getChannels() {
        return getChannels(true);
    }

    public List<VoiceChannel> getVoiceChannels() {
        return VoiceChannel.getVoiceChannels(shard().getVoiceChannels());
    }

    public List<VoiceChannel> getConnectedVoiceChannels() {
        return VoiceChannel.getVoiceChannels(shard().getConnectedVoiceChannels());
    }

    public List<Guild> getGuilds() {
        return Guild.getGuilds(shard().getGuilds());
    }

    public List<User> getUsers() {
        return User.getUsers(shard().getUsers());
    }

    public List<Role> getRoles() {
        return Role.getRoles(shard().getRoles());
    }
}
