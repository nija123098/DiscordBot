package com.github.kaaz.discordbot.discordobjects.wrappers;

import sx.blah.discord.handle.obj.IRegion;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Made by nija123098 on 3/9/2017.
 */
public class Region {
    private static final Map<String, Region> MAP = new ConcurrentHashMap<>();
    public static Region getRegion(String id){// todo replace null
        return MAP.computeIfAbsent(id, s -> null);
    }
    static Region getRegion(IRegion region){
        return MAP.computeIfAbsent(region.getID(), s -> new Region(region));
    }
    static List<Region> getRegions(List<IRegion> iRegions){
        List<Region> list = new ArrayList<>(iRegions.size());
        iRegions.forEach(iRegion -> list.add(getRegion(iRegion)));
        return list;
    }
    public synchronized void update(IRegion region){
        MAP.get(region.getID()).reference.set(region);
    }
    private final AtomicReference<IRegion> reference;
    private Region(IRegion region) {
        this.reference = new AtomicReference<>(region);
    }
    IRegion region(){
        return this.reference.get();
    }
    public String getID() {
        return region().getID();
    }

    public String getName() {
        return region().getName();
    }

    public boolean isVIPOnly() {
        return region().isVIPOnly();
    }
}
