package com.github.nija123098.evelyn.perms;

import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.Configurable;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordPermission;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventDistributor;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.botevents.BotRoleChangeEvent;
import com.github.nija123098.evelyn.exception.ArgumentException;
import com.github.nija123098.evelyn.exception.PermissionsException;
import com.github.nija123098.evelyn.launcher.Launcher;
import com.github.nija123098.evelyn.perms.configs.standard.GlobalBotRoleConfig;
import com.github.nija123098.evelyn.perms.configs.standard.GuildBotRoleConfig;
import com.github.nija123098.evelyn.util.CacheHelper;
import com.google.common.cache.LoadingCache;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

/**
 * The enum to represent a permissions level for a {@link User} or {@link GuildUser}.
 * There are two kinds of ranks, inferred, and flag.
 *
 * Inferred ranks come are determined from the {@link User} and the {@link Guild} context.
 * Flag ranks are set for a {@link User} by another {@link User} with a higher rank.
 *
 * @author nija123098
 * @since 1.0.0
 */
public enum BotRole {// todo make more efficient
    BOT(true, (user, guild) -> user.isBot()),
    BANNED(true, true, false, WorkAroundReferences.B_A),
    USER(true, (user, guild) -> !BOT.hasRole(user, guild) && !BANNED.hasRole(user, guild)),
    GUILD_DJ(false, false, true, WorkAroundReferences.G_A),
    GUILD_TRUSTEE(true, false, true, WorkAroundReferences.G_A),
    GUILD_ADMIN(true, (user, guild) -> guild != null && USER.hasRole(user, null) && user.getPermissionsForGuild(guild).contains(DiscordPermission.ADMINISTRATOR)),
    GUILD_OWNER(true, (user, guild) -> guild != null && USER.hasRole(user, null) && guild.getOwner().equals(user)),
    SUPPORTER(false, true, false, WorkAroundReferences.B_A),
    CONTRIBUTOR(false, true, false, WorkAroundReferences.B_O),
    BOT_ADMIN(true, true, false, WorkAroundReferences.B_O),
    BOT_OWNER(true, (user, guild) -> user.equals(DiscordClient.getApplicationOwner())),
    SYSTEM(true, (user, guild) -> Launcher.hasSystemAccess(user)),;
    static {
        WorkAroundReferences.set();
    }
    private final Map<Object, Object> PERMISSIONS_CACHE = new HashMap<>();
    private final LoadingCache<User, Boolean> userCache;
    private final LoadingCache<User, LoadingCache<Guild, Boolean>> guildCache;
    private boolean isTrueRank, isGlobalFlag, isGuildFlag, guildImportant = this.name().startsWith("GUILD");
    private BiPredicate<User, Guild> detect, change;
    BotRole(boolean isTrueRank, BiPredicate<User, Guild> detect, BiPredicate<User, Guild> change) {
        this();
        this.isTrueRank = isTrueRank;
        this.detect = detect;
        this.change = change;
    }
    BotRole(boolean isTrueRank, BiPredicate<User, Guild> detect) {
        this(isTrueRank, detect, (user, guild) -> false);
    }
    BotRole(boolean isTrueRank, boolean isGlobalFlag, boolean isGuildFlag, AtomicReference<BotRole> required) {
        this();
        this.isTrueRank = isTrueRank;
        this.isGlobalFlag = isGlobalFlag;
        this.isGuildFlag = isGuildFlag;
        this.detect = (user, guild) -> (this.isGlobalFlag && ConfigHandler.getSetting(GlobalBotRoleConfig.class, user).contains(this)) || (this.isGuildFlag && guild != null && ConfigHandler.getSetting(GuildBotRoleConfig.class, GuildUser.getGuildUser(guild, user)).contains(this));
        this.change = (user, guild) -> required.get().hasRequiredRole(user, guild);// method reference doesn't work
    }
    BotRole() {
        if (this.guildImportant) {
            this.userCache = null;
            this.guildCache = CacheHelper.getLoadingCache(Runtime.getRuntime().availableProcessors() * 4, ConfigProvider.CACHE_SETTINGS.userBotRoleSize(), 120_000, user -> CacheHelper.getLoadingCache(Runtime.getRuntime().availableProcessors(), 20, 120_000, guild -> {
                for (int i = this.ordinal(); i < values().length; i++) if (values()[i].detect.test(user, guild)) return true;
                return false;
            }));
        } else {
            this.userCache = CacheHelper.getLoadingCache(Runtime.getRuntime().availableProcessors() * 4, 20, 300_000, user -> {
                for (int i = this.ordinal(); i < values().length; i++) if (values()[i].detect.test(user, null)) return true;
                return false;
            });
            this.guildCache = null;
        }
    }

    /**
     * Gets if the rank is set by a higher role.
     *
     * @return if the rank is set by a higher role
     */
    public boolean isFlagRank(){
        return this.isGlobalFlag || this.isGuildFlag;
    }

    /**
     * Checks if the user has the exact role.
     *
     * @param user the {@link User} to get if they have the instance role.
     * @param guild the {@link Guild} context to infer if a {@link User} has a certain role.
     * @return if the user has the exact role.
     */
    public boolean hasRole(User user, Guild guild){
        return this.detect.test(user, guild);
    }

    /**
     * Gets if the user has the required role or above.
     *
     * If the instance role is a flag role it checks {@link BotRole#hasRole(User, Guild)},
     * otherwise the method checks if the user has any true ranks in ascending order.
     *
     * @param user the {@link User} to get if they have the instance role.
     * @param guild the {@link Guild} context to infer if a {@link User} has a certain role.
     * @return if the user has the role or a higher true role.
     */
    public boolean hasRequiredRole(User user, Guild guild){
        return this.isTrueRank ? this.guildImportant ? this.guildCache.getUnchecked(user).getUnchecked(guild) : this.userCache.getUnchecked(user) : this.detect.test(user, guild);
    }

    /**
     * A form of {@link BotRole#hasRequiredRole(User, Guild)} which throws
     * a {@link PermissionsException} if the result of the check is false.
     *
     * @param user the {@link User} to get if they have the instance role.
     * @param guild the {@link Guild} context to infer if a {@link User} has a certain role.
     * @throws PermissionsException is thrown if the result of the
     * {@link BotRole#hasRequiredRole(User, Guild)} is false.
     */
    public void checkRequiredRole(User user, Guild guild){
        if (!this.hasRequiredRole(user, guild)) throw new PermissionsException(this);
    }

    /**
     * Gets a set of roles which defines the permissions
     * for the given {@link User} in the given {@link Guild}.
     *
     * @param user the {@link User} to get if they have the instance role.
     * @param guild the {@link Guild} context to infer if a {@link User} has a certain role.
     * @return the set of roles a user has permissions as.
     */
    public static EnumSet<BotRole> getSet(User user, Guild guild){
        EnumSet<BotRole> set = EnumSet.noneOf(BotRole.class);
        for (BotRole role : values()){
            if (role.hasRole(user, guild)){
                set.add(role);
            }
        }
        return set;
    }

    /**
     * Sets a user's flag rank first preforming a check if the setting
     * {@link User} has permission to set the {@link User} to set the role.
     *
     * @param role the role to give or take from the target.
     * @param grant if the role should be granted or taken away.
     * @param target the {@link User} to effect {@link BotRole}s for.
     * @param setter the {@link User} who is modifying the roles.
     * @param guild the context in which the rank is being set.
     */
    public static void setRole(BotRole role, boolean grant, User target, User setter, Guild guild){
        if (!role.isFlagRank()) throw new ArgumentException("You can not set non-flag roles though a bot");
        if (!role.change.test(setter, guild)) throw new PermissionsException("You can not change that role");
        setRole(role, grant, target, guild);
    }

    /**
     * Sets a user's flag rank without preforming a permission checks.
     *
     * @param role the role to grant or take away.
     * @param grant if the role should be granted or taken away.
     * @param target the {@link User} to effect {@link BotRole}s for.
     * @param guild the context in which the rank is being set.
     */
    public static void setRole(BotRole role, boolean grant, User target, Guild guild){
        Class<? extends AbstractConfig<Set<BotRole>, ? extends Configurable>> config = role.isGlobalFlag ? GlobalBotRoleConfig.class : GuildBotRoleConfig.class;
        Configurable configurable = role.isGlobalFlag ? target : GuildUser.getGuildUser(guild, target);
        ConfigHandler.alterSetting((Class<? extends AbstractConfig<Set<BotRole>, Configurable>>) config, configurable, roles -> {
            if (grant) roles.add(role);
            else roles.remove(role);
        });
        Stream.of(values()).forEach(botRole -> botRole.PERMISSIONS_CACHE.clear());
        EventDistributor.distribute(new BotRoleChangeEvent(grant, role, target, guild));
    }
}
