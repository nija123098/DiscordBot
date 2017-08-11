package com.github.kaaz.emily.favor;

import com.github.kaaz.emily.audio.Track;
import com.github.kaaz.emily.audio.configs.track.PlayCountConfig;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.ConfigLevel;
import com.github.kaaz.emily.config.Configurable;
import com.github.kaaz.emily.config.GuildUser;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.discordobjects.wrappers.event.EventDistributor;
import com.github.kaaz.emily.discordobjects.wrappers.event.EventListener;
import com.github.kaaz.emily.discordobjects.wrappers.event.botevents.FavorLevelChangeEvent;
import com.github.kaaz.emily.exeption.DevelopmentException;
import com.github.kaaz.emily.favor.configs.GuildUserReputationConfig;
import com.github.kaaz.emily.favor.configs.balencing.MessageFavorFactorConfig;
import com.github.kaaz.emily.favor.configs.balencing.ReactionFavorFactorConfig;
import com.github.kaaz.emily.favor.configs.balencing.ReputationFavorFactorConfig;
import com.github.kaaz.emily.favor.configs.derivation.BannedLocationConfig;
import com.github.kaaz.emily.favor.configs.derivation.MessageCountConfig;
import com.github.kaaz.emily.favor.configs.derivation.ReactionCountConfig;
import com.github.kaaz.emily.perms.BotRole;
import com.google.common.util.concurrent.AtomicDouble;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * The handler for favor levels for guilds and users.
 *
 * @author nija123098
 * @since 2.0.0
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
            return v;
        });
        add(User.class, user -> {
            AtomicDouble value = new AtomicDouble();
            user.getGuilds().forEach(guild -> value.addAndGet(getFavorAmount(guild)));
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
     * @param configurable the guild or user to get the favor amount for
     * @return the favor amount
     */
    public static Float getFavorAmount(Configurable configurable){
        Function<Configurable, Float> function = (Function<Configurable, Float>) TYPE_DERIVATIONS.get(configurable.getConfigLevel());
        if (function == null) throw new DevelopmentException("Request for favor on type with no ");
        return function.apply(configurable);
    }
    /**
     * Gets the enum by the favor amount indicated.
     *
     * @param configurable the guild or user to get the favor level
     * @return the corresponding favor enum
     */
    public static FavorLevel getFavorLevel(Configurable configurable){
        return FavorLevel.getFavorLevel(getFavorAmount(configurable));
    }
    static {EventDistributor.register(FavorHandler.class);}
    @EventListener
    public static void handle(FavorLevelChangeEvent event){
        if (event.getNewLevel() == FavorLevel.DISTRUSTED && event.getConfigurable() instanceof User){
            BotRole.setRole(BotRole.BANNED, true, (User) event.getConfigurable(), null);
        }
    }
}
