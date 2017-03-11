package com.github.kaaz.discordbot.discordobjects.wrappers;

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
    private static final Map<String, Shard> MAP = new ConcurrentHashMap<>();
    public static Shard getShard(String id){// todo replace null
        return MAP.computeIfAbsent(id, s -> null);
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
}
