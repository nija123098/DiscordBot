package com.github.nija123098.evelyn.audio.configs.track;

import com.github.nija123098.evelyn.audio.Track;
import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.perms.BotRole;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TrackAssociationConfig extends AbstractConfig<Map<Track, Integer>, Track> {
    private static TrackAssociationConfig config;
    public TrackAssociationConfig() {
        super("track_similarities", ConfigCategory.STAT_TRACKING, new ConcurrentHashMap<>(), "Tracks similarities to other tracks");
        config = this;
    }
    public static void associate(Track first, Track second){
        associateOne(first, second);
        associateOne(second, first);
    }
    private static void associateOne(Track one, Track other){
        config.alterSetting(one, trackIntegerMap -> trackIntegerMap.compute(other, (track, integer) -> integer == null ? 1 : ++integer));
    }
}
