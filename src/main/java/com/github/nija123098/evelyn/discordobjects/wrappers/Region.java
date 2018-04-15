package com.github.nija123098.evelyn.discordobjects.wrappers;

import sx.blah.discord.handle.obj.IRegion;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Wraps a Discord4j {@link IRegion} object.
 *
 * @author nija123098
 * @since 1.0.0
 */
public class Region {
    private static final Map<String, Region> MAP = new ConcurrentHashMap<>(15);// never needs to be cleared
    public static Region getRegion(String id) {
        return getRegion((IRegion) DiscordClient.getAny(client -> client.getRegionByID(id)));
    }
    static Region getRegion(IRegion region) {
        if (region == null) return null;
        return MAP.computeIfAbsent(region.getID(), s -> new Region(region));
    }
    static List<Region> getRegions(List<IRegion> iRegions) {
        List<Region> list = new ArrayList<>(iRegions.size());
        iRegions.forEach(iRegion -> list.add(getRegion(iRegion)));
        return list;
    }
    private final AtomicReference<IRegion> reference;
    private Region(IRegion region) {
        this.reference = new AtomicReference<>(region);
    }
    IRegion region() {
        return this.reference.get();
    }
    public String getID() {
        return region().getID();
    }

    @Override
    public boolean equals(Object o) {
        return o == this || o instanceof Region && ((Region) o).getID().equals(this.getID());
    }

    @Override
    public int hashCode() {
        return this.region().hashCode();
    }

    public String getName() {
        return region().getName();
    }

    public boolean isVIPOnly() {
        return region().isVIPOnly();
    }
}
