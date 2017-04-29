package com.github.kaaz.emily.perms;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.Configurable;
import com.github.kaaz.emily.config.GuildUser;
import com.github.kaaz.emily.discordobjects.wrappers.DiscordClient;
import com.github.kaaz.emily.discordobjects.wrappers.DiscordPermission;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.exeption.ArgumentException;
import com.github.kaaz.emily.exeption.PermissionsException;
import com.github.kaaz.emily.perms.configs.standard.GlobalBotRoleConfig;
import com.github.kaaz.emily.perms.configs.standard.GuildBotRoleConfig;

import java.util.EnumSet;
import java.util.Set;
import java.util.function.BiPredicate;

/**
 * Made by nija123098 on 2/20/2017.
 */
public enum BotRole {
    BOT(true, (user, guild) -> user.isBot()),
    BANNED(true, true, false, (user, guild) -> hasRequiredRole(WorkAroundReferences.B_A, user, null)),
    INTERACTION_BOT(true, true, false, (user, guild) -> hasRequiredRole(WorkAroundReferences.B_A, user, null)),
    USER(true, (user, guild) -> !hasRole(BOT, user, guild) && !hasRole(BANNED, user, guild)),
    SUPPORTER(false, false, true, (user, guild) -> hasRequiredRole(WorkAroundReferences.B_A, user, null)),
    CONTRIBUTOR(false, true, false, (user, guild) -> hasRequiredRole(WorkAroundReferences.B_A, user, null)),
    GUILD_TRUSTEE(true, false, true, (user, guild) -> hasRequiredRole(WorkAroundReferences.G_A, user, null)),
    GUILD_ADMIN(true, (user, guild) -> hasRole(USER, user, guild) && user.getPermissionsForGuild(guild).contains(DiscordPermission.ADMINISTRATOR)),
    GUILD_OWNER(true, (user, guild) -> guild.getOwner().equals(user)),
    BOT_ADMIN(true, true, false, (user, guild) -> hasRequiredRole(WorkAroundReferences.B_O, user, null)),
    BOT_OWNER(true, (user, guild) -> user.equals(DiscordClient.getApplicationOwner())),
    SYSTEM(true, (user, guild) -> false),;
    private boolean isTrueRank, isGlobalFlag, isGuildFlag;
    private BiPredicate<User, Guild> detect, change;
    BotRole(boolean isTrueRank, BiPredicate<User, Guild> detect, BiPredicate<User, Guild> change) {
        this.isTrueRank = isTrueRank;
        this.detect = detect;
        this.change = change;
    }
    BotRole(boolean isTrueRank, BiPredicate<User, Guild> detect) {
        this(isTrueRank, detect, (user, guild) -> false);
    }
    BotRole(boolean isTrueRank, boolean isGlobalFlag, boolean isGuildFlag, BiPredicate<User, Guild> change) {
        this.isTrueRank = isTrueRank;
        this.isGlobalFlag = isGlobalFlag;
        this.isGuildFlag = isGuildFlag;
        this.detect = (user, guild) -> (this.isGlobalFlag && ConfigHandler.getSetting(GlobalBotRoleConfig.class, user).contains(this)) || (this.isGuildFlag && guild != null && ConfigHandler.getSetting(GuildBotRoleConfig.class, GuildUser.getGuildUser(guild, user)).contains(this));
        this.change = change;
    }
    public boolean isFlagRank(){
        return this.isGlobalFlag || this.isGuildFlag;
    }
    public static boolean hasRole(BotRole role, User user, Guild guild){
        return role.detect.test(user, guild);
    }
    public static boolean hasRequiredRole(BotRole role, User user, Guild guild){
        if (!role.isTrueRank){
            return role.detect.test(user, guild);
        }
        for (int i = role.ordinal(); i < values().length; i++) {
            if (role.isTrueRank && values()[i].detect.test(user, guild)){
                return true;
            }
        }
        return false;
    }
    public static void checkRequiredRole(BotRole role, User user, Guild guild){
        if (!hasRequiredRole(role, user, guild)){
            throw new PermissionsException(role);
        }
    }
    public static EnumSet<BotRole> getSet(User user, Guild guild){
        EnumSet<BotRole> set = EnumSet.noneOf(BotRole.class);
        for (BotRole role : values()){
            if (hasRole(role, user, guild)){
                set.add(role);
            }
        }
        return set;
    }
    public static void setRole(BotRole role, boolean grant, User target, User setter, Guild guild){
        if (!role.isFlagRank()) throw new ArgumentException("You can not set non-flag roles though a bot");
        if (role.change.test(setter, guild)) throw new PermissionsException("You can not change that role");
        setRole(role, grant, target, guild);
    }// this might just get moved to a command
    public static void setRole(BotRole role, boolean grant, User target, Guild guild){
        Class<? extends AbstractConfig<Set<BotRole>, ? extends Configurable>> config = role.isGlobalFlag ? GlobalBotRoleConfig.class : GuildBotRoleConfig.class;
        Configurable configurable = role.isGlobalFlag ? target : GuildUser.getGuildUser(guild, target);
        Set<BotRole> roles = ConfigHandler.getSetting((Class<? extends AbstractConfig<Set<BotRole>,Configurable>>) config, configurable);
        if (grant) roles.add(role);
        else roles.remove(role);
        ConfigHandler.setSetting((Class<? extends AbstractConfig<Set<BotRole>,Configurable>>) config, configurable, roles);
    }
}
