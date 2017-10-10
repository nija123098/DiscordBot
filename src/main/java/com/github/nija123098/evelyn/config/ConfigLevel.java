package com.github.nija123098.evelyn.config;

import com.github.nija123098.evelyn.audio.Playlist;
import com.github.nija123098.evelyn.audio.Track;
import com.github.nija123098.evelyn.discordobjects.wrappers.*;
import com.github.nija123098.evelyn.exeption.DevelopmentException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The enum to represent a type of configurable object.
 * Objects that can be configured must have a type.
 *
 * @author nija123098
 * @since 1.0.0
 */
@ConfigurableTypeAddLocation("A enum representation must be added")
public enum ConfigLevel {
    /** The enum representation for audio tracks */
    TRACK(Track.class, false),
    /** The enum representation for any playlist type */
    PLAYLIST(Playlist.class, false),
    /** The enum representation for a user's config */
    USER(User.class, false),
    /** The enum representation for a channel's config */
    CHANNEL(Channel.class, true),
    /** The enum representation for a category's config */
    CATEGORY(Category.class, true),
    /** The enum representation for a user's config within a guild */
    GUILD_USER(GuildUser.class, true),
    /** The enum representation for a role within a guild */
    ROLE(Role.class, true),
    /** The enum representation for a guild's config */
    GUILD(Guild.class, true),
    /** The enum representation for global config */
    GLOBAL(GlobalConfigurable.class, false),
    /** The enum representation for a config that applies to all configurable types */
    ALL(Configurable.class, false),;
    private final Class<? extends Configurable> clazz;
    private final Set<ConfigLevel> assignables = new HashSet<>();
    private final boolean mayCache;
    ConfigLevel(Class<? extends Configurable> clazz, boolean mayCache) {
        this.clazz = clazz;
        this.mayCache = mayCache;
    }
    public Class<? extends Configurable> getType(){
        return this.clazz;
    }
    public Set<ConfigLevel> getAssignable(){
        return assignables;
    }
    public boolean mayCache(){
        return this.mayCache;
    }
    public boolean isAssignableFrom(ConfigLevel level){
        return this == ALL || this == level;
    }
    public static void load(){
        for (ConfigLevel configLevel : ConfigLevel.values()) for (ConfigLevel level : ConfigLevel.values()) if (configLevel.isAssignableFrom(level) && level != ALL) configLevel.assignables.add(level);
    }
    public static ConfigLevel getLevel(Class<? extends Configurable> clazz){
        for (ConfigLevel level : values()){
            if (level.clazz.isAssignableFrom(clazz)){
                return level;
            }
        }
        throw new DevelopmentException("Class does not have a type: " + clazz.getName());
    }
    private static List<ConfigLevel> levels = Stream.of(values()).filter(configLevel -> configLevel != ALL).collect(Collectors.toList());
    public static List<ConfigLevel> nonAllValues(){
        return levels;
    }
}
