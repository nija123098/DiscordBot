package com.github.kaaz.emily.perms;

import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.Configurable;
import com.github.kaaz.emily.config.configs.guilduser.GuildBotRoleConfig;
import com.github.kaaz.emily.config.configs.user.GlobalBotRoleConfig;
import com.github.kaaz.emily.discordobjects.wrappers.DiscordClient;
import com.github.kaaz.emily.discordobjects.wrappers.DiscordPermission;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.User;

import java.util.EnumSet;
import java.util.function.BiPredicate;

/**
 * Made by nija123098 on 2/20/2017.
 */
public enum BotRole {
    BOT(true, (user, guild) -> user.isBot()),
    BANNED(true, true, false),
    INTERACTION_BOT(true, true, false),
    USER(true, (user, guild) -> !hasRole(BOT, user, guild) && !hasRole(BANNED, user, guild)),
    CONTRIBUTOR(false, true, false),
    GUILD_TRUSTEE(true, false, true),
    GUILD_ADMIN(true, (user, guild) -> hasRole(USER, user, guild) && user.getPermissionsForGuild(guild).contains(DiscordPermission.ADMINISTRATOR)),
    GUILD_OWNER(true, (user, guild) -> guild.getOwner().equals(user)),
    BOT_ADMIN(true, true, false),
    BOT_OWNER(true, (user, guild) -> user.equals(DiscordClient.getApplicationOwner())),
    SYSTEM(true, (user, guild) -> false),;
    private boolean isTrueRank, isGlobalFlag, isGuildFlag;
    private BiPredicate<User, Guild> predicate;
    BotRole(boolean isTrueRank, BiPredicate<User, Guild> predicate) {
        this.isTrueRank = isTrueRank;
        this.predicate = predicate;
    }
    BotRole(boolean isTrueRank, boolean isGlobalFlag, boolean isGuildFlag) {
        this.isTrueRank = isTrueRank;
        this.isGlobalFlag = isGlobalFlag;
        this.isGuildFlag = isGuildFlag;
        this.predicate = (user, guild) -> (this.isGlobalFlag && ConfigHandler.getSetting(GlobalBotRoleConfig.class, user).contains(this.name())) || (this.isGuildFlag && guild != null && ConfigHandler.getSetting(GuildBotRoleConfig.class, Configurable.getGuildUser(guild, user)).contains(this.name()));
    }
    public static boolean hasRole(BotRole role, User user, Guild guild){
        return role.predicate.test(user, guild);
    }
    public static boolean hasRequiredRole(BotRole role, User user, Guild guild){
        for (int i = role.ordinal(); i < values().length; i++) {
            if (role.isTrueRank && values()[i].predicate.test(user, guild)){
                return true;
            }
        }
        return false;
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
}
