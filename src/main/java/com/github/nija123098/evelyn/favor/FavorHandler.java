package com.github.nija123098.evelyn.favor;

import com.github.nija123098.evelyn.audio.Track;
import com.github.nija123098.evelyn.audio.configs.track.PlayCountConfig;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.ConfigLevel;
import com.github.nija123098.evelyn.config.Configurable;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventDistributor;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.botevents.FavorLevelChangeEvent;
import com.github.nija123098.evelyn.exception.DevelopmentException;
import com.github.nija123098.evelyn.favor.configs.GuildUserReputationConfig;
import com.github.nija123098.evelyn.favor.configs.balencing.*;
import com.github.nija123098.evelyn.favor.configs.derivation.*;
import com.github.nija123098.evelyn.perms.BotRole;
import com.google.common.util.concurrent.AtomicDouble;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * The handler for favor levels.
 *
 * @author nija123098
 * @since 1.0.0
 * @see FavorLevel
 */
public class FavorHandler {
    private static final Map<ConfigLevel, Function<? extends Configurable, Float>> TYPE_DERIVATIONS = new HashMap<>();
    static {
        add(GuildUser.class, guildUser -> {
            if (ConfigHandler.getSetting(BannedLocationConfig.class, guildUser.getUser()).contains(guildUser.getGuild())) return -5000F;
            float v = ConfigHandler.getSetting(MessageFavorFactorConfig.class, guildUser.getGuild()) * ConfigHandler.getSetting(MessageCountConfig.class, guildUser);
            v += ConfigHandler.getSetting(ReputationFavorFactorConfig.class, guildUser.getGuild()) * ConfigHandler.getSetting(GuildUserReputationConfig.class, guildUser);
            v += ConfigHandler.getSetting(ReactionFavorFactorConfig.class, guildUser.getGuild()) * ConfigHandler.getSetting(ReactionCountConfig.class, guildUser);
            v += ConfigHandler.getSetting(VoiceTimeFavorFactorConfig.class, guildUser.getGuild()) * ConfigHandler.getSetting(VoiceTimeConfig.class, guildUser);
            v += ConfigHandler.getSetting(GameTimeFavorFactorConfig.class, guildUser.getGuild()) * ConfigHandler.getSetting(PlayTimeFavorConfig.class, guildUser);
            v += ConfigHandler.getSetting(TimeFavorFactorConfig.class, guildUser.getGuild()) * (System.currentTimeMillis() - guildUser.getGuild().getJoinTimeForUser(guildUser.getUser())) / 900000;
            return v;
        });
        add(User.class, user -> {
            AtomicDouble value = new AtomicDouble();
            user.getGuilds().forEach(guild -> value.addAndGet(getFavorAmount(GuildUser.getGuildUser(guild, user))));
            return (float) value.get() / user.getGuilds().size();
        });
        add(Track.class, track -> ConfigHandler.getSetting(PlayCountConfig.class, track).floatValue());
    }
    private static <E extends Configurable> void add(Class<E> clazz, Function<E, Float> function){
        TYPE_DERIVATIONS.put(ConfigLevel.getLevel(clazz), function);
    }
    /**
     * A getter for the FavorLevel config.
     *
     * @param configurable the guild or user to get the favor amount for.
     * @return the favor amount.
     */
    public static Float getFavorAmount(Configurable configurable){
        Function<Configurable, Float> function = (Function<Configurable, Float>) TYPE_DERIVATIONS.get(configurable.getConfigLevel());
        if (function == null) throw new DevelopmentException("Request for favor on type with no favor calculation available: " + configurable.getConfigLevel());
        return function.apply(configurable);
    }
    /**
     * Gets the enum by the favor amount indicated.
     *
     * @param configurable the guild or user to get the favor level.
     * @return the corresponding favor enum.
     */
    public static FavorLevel getFavorLevel(Configurable configurable){
        return FavorLevel.getFavorLevel(getFavorAmount(configurable));
    }
    static {
        EventDistributor.register(FavorHandler.class);
    }
    @EventListener
    public static void handle(FavorLevelChangeEvent event){
        if (event.getNewLevel() == FavorLevel.DISTRUSTED && event.getConfigurable() instanceof User){
            BotRole.setRole(BotRole.BANNED, true, (User) event.getConfigurable(), null);
        }
    }
}
