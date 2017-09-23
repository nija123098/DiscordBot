package com.github.nija123098.evelyn.perms;

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
import com.github.nija123098.evelyn.exeption.ArgumentException;
import com.github.nija123098.evelyn.exeption.PermissionsException;
import com.github.nija123098.evelyn.launcher.Launcher;
import com.github.nija123098.evelyn.perms.configs.standard.GlobalBotRoleConfig;
import com.github.nija123098.evelyn.perms.configs.standard.GuildBotRoleConfig;
import com.github.nija123098.evelyn.service.services.ScheduleService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

/**
 * Made by nija123098 on 2/20/2017.
 */
public enum BotRole {
    BOT(true, (user, guild) -> user.isBot()),
    BANNED(true, true, false, WorkAroundReferences.B_A),
    INTERACTION_BOT(true, true, false, WorkAroundReferences.B_A),
    USER(true, (user, guild) -> !BOT.hasRole(user, guild) && !BANNED.hasRole(user, guild)),
    GUILD_TRUSTEE(true, false, true, WorkAroundReferences.G_A),
    GUILD_ADMIN(true, (user, guild) -> guild != null && USER.hasRole(user, null) && user.getPermissionsForGuild(guild).contains(DiscordPermission.ADMINISTRATOR)),
    GUILD_OWNER(true, (user, guild) -> guild != null && guild.getOwner().equals(user)),
    SUPPORTER(false, true, false, WorkAroundReferences.B_A),
    CONTRIBUTOR(false, true, false, WorkAroundReferences.B_A),
    BOT_ADMIN(true, true, false, WorkAroundReferences.B_O),
    BOT_OWNER(true, (user, guild) -> user.equals(DiscordClient.getApplicationOwner())),
    SYSTEM(true, (user, guild) -> Launcher.hasSystemAccess(user)),;
    static {
        WorkAroundReferences.set();
    }
    private boolean isTrueRank, isGlobalFlag, isGuildFlag;// , guildImportant = this.name().startsWith("GUILD");
    private BiPredicate<User, Guild> detect, change;
    BotRole(boolean isTrueRank, BiPredicate<User, Guild> detect, BiPredicate<User, Guild> change) {
        this.isTrueRank = isTrueRank;
        this.detect = detect;
        this.change = change;
    }
    BotRole(boolean isTrueRank, BiPredicate<User, Guild> detect) {
        this(isTrueRank, detect, (user, guild) -> false);
    }
    BotRole(boolean isTrueRank, boolean isGlobalFlag, boolean isGuildFlag, AtomicReference<BotRole> required) {
        this.isTrueRank = isTrueRank;
        this.isGlobalFlag = isGlobalFlag;
        this.isGuildFlag = isGuildFlag;
        this.detect = (user, guild) -> (this.isGlobalFlag && ConfigHandler.getSetting(GlobalBotRoleConfig.class, user).contains(this)) || (this.isGuildFlag && guild != null && ConfigHandler.getSetting(GuildBotRoleConfig.class, GuildUser.getGuildUser(guild, user)).contains(this));
        this.change = (user, guild) -> required.get().hasRequiredRole(user, guild);// method reference doesn't work
    }
    public boolean isFlagRank(){
        return this.isGlobalFlag || this.isGuildFlag;
    }
    public boolean hasRole(User user, Guild guild){
        return this.detect.test(user, guild);
    }
    // private final Map<Object, Object> PERMISSIONS_CACHE = new HashMap<>();
    public boolean hasRequiredRole(User user, Guild guild){
        if (!this.isTrueRank) return this.detect.test(user, guild);
        /* return (boolean) (this.guildImportant ? ((Map<Object, Object>) PERMISSIONS_CACHE.computeIfAbsent(guild, g -> new ConcurrentHashMap<>())) : PERMISSIONS_CACHE).computeIfAbsent(user, u -> {
            ScheduleService.schedule(120_000, () -> (this.guildImportant ? (Map<Object, Object>) PERMISSIONS_CACHE.get(guild) : PERMISSIONS_CACHE).remove(user));
        }); todo reimplement caching*/
        for (int i = this.ordinal(); i < values().length; i++) if (values()[i].detect.test(user, guild)) return true;
        return false;
    }
    public void checkRequiredRole(User user, Guild guild){
        if (!this.hasRequiredRole(user, guild)) throw new PermissionsException(this);
    }
    public static EnumSet<BotRole> getSet(User user, Guild guild){
        EnumSet<BotRole> set = EnumSet.noneOf(BotRole.class);
        for (BotRole role : values()){
            if (role.hasRole(user, guild)){
                set.add(role);
            }
        }
        return set;
    }
    public static void setRole(BotRole role, boolean grant, User target, User setter, Guild guild){
        if (!role.isFlagRank()) throw new ArgumentException("You can not set non-flag roles though a bot");
        if (!role.change.test(setter, guild)) throw new PermissionsException("You can not change that role");
        setRole(role, grant, target, guild);
    }// this might just get moved to a command
    public static void setRole(BotRole role, boolean grant, User target, Guild guild){
        Class<? extends AbstractConfig<Set<BotRole>, ? extends Configurable>> config = role.isGlobalFlag ? GlobalBotRoleConfig.class : GuildBotRoleConfig.class;
        Configurable configurable = role.isGlobalFlag ? target : GuildUser.getGuildUser(guild, target);
        ConfigHandler.alterSetting((Class<? extends AbstractConfig<Set<BotRole>, Configurable>>) config, configurable, roles -> {
            if (grant) roles.add(role);
            else roles.remove(role);
        });
        // Stream.of(values()).forEach(botRole -> botRole.PERMISSIONS_CACHE.clear());
        EventDistributor.distribute(new BotRoleChangeEvent(grant, role, target, guild));
    }
}
