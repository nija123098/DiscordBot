package com.github.kaaz.emily.discordobjects.wrappers;

import sx.blah.discord.api.IShard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Made by nija123098 on 2/27/2017.
 */
public class Shard {
    private static final Map<Integer, Shard> MAP = new ConcurrentHashMap<>();// never clear
    /*static {
        for (IShard shard : DiscordClient.client().getShards()){// list not guaranteed to be in order
            MAP.put(shard.getInfo()[0], new Shard(shard));
        }
    }*/
    public static Shard getShard(int i){
        return getShard(DiscordClient.client().getShards().get(i));
    }
    static Shard getShard(IShard shard){
        return MAP.computeIfAbsent(shard.getInfo()[0], integer -> new Shard(shard));
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
    public int getID(){
        return shard().getInfo()[0];
    }

    @Override
    public boolean equals(Object o) {
        return o == this || o instanceof Shard && ((Shard) o).getID() == this.getID();
    }

    @Override
    public int hashCode() {
        return this.getID();
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
