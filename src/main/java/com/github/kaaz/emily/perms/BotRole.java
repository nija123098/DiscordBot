package com.github.kaaz.emily.perms;

import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.Configurable;
import com.github.kaaz.emily.config.configs.guilduser.GuildFlagRankConfig;
import com.github.kaaz.emily.config.configs.user.GlobalBotRoleFlagConfig;
import com.github.kaaz.emily.discordobjects.wrappers.DiscordClient;
import com.github.kaaz.emily.discordobjects.wrappers.DiscordPermission;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.util.Log;

/**
 * Made by nija123098 on 2/20/2017.
 */
public enum BotRole {// todo clean
    BOT,
    BANNED(true, true, false),
    INTERACTION_BOT(true, true, false),
    USER,
    CONTRIBUTOR(false, true, false),
    GUILD_TRUSTEE(true, false, true),
    GUILD_ADMIN,
    GUILD_OWNER,
    BOT_ADMIN(true, true, false),
    BOT_OWNER,;
    boolean isTrueRank, isGlobalFlag, isGuildFlag;
    BotRole(boolean isTrueRank, boolean isGlobalFlag, boolean isGuildFlag) {
        this.isTrueRank = isTrueRank;
        this.isGlobalFlag = isGlobalFlag;
        this.isGuildFlag = isGuildFlag;
    }
    BotRole(){
        this(true, false, false);
    }
    public boolean isFlagRank(){
        return this.isGlobalFlag || this.isGuildFlag;
    }
    public static boolean hasRequiredBotRole(BotRole target, User user, Guild guild){
        if (DiscordClient.getApplicationOwner().equals(user)){
            return true;
        }
        if (hasFlagRank(BANNED, user, null)){
            return false;
        }
        if (target.isFlagRank()){
            return hasFlagRank(target, user, guild);
        }
        return getStandardBotRole(user, guild).ordinal() >= target.ordinal();
    }
    private static BotRole getStandardBotRole(User user, Guild guild){
        if (user.isBot()){
            return BOT;
        }
        if (guild != null){
            if (guild.getOwner().equals(user)){
                return GUILD_OWNER;
            }
            if (user.getPermissionsForGuild(guild).contains(DiscordPermission.ADMINISTRATOR)){
                return GUILD_ADMIN;
            }
        }
        return USER;
    }
    private static boolean hasFlagRank(BotRole target, User user, Guild guild){
        if (target.isGlobalFlag){
            return ConfigHandler.getSetting(GlobalBotRoleFlagConfig.class, user).contains(target.name().toLowerCase());
        } else if (target.isGuildFlag){
            return ConfigHandler.getSetting(GuildFlagRankConfig.class, Configurable.getGuildUser(guild, user)).contains(target.name().toLowerCase());
        } else {
            Log.log("Tried checking a non-flag rank for having the flag");
            return false;
        }
    }
    public static BotRole getBestBotRole(User user, Guild guild){
        BotRole role = getStandardBotRole(user, guild);
        for (int i = role.ordinal(); i < values().length; i++) {
            if (values()[i].isFlagRank()){
                if (hasFlagRank(values()[i], user, guild)){
                    role = values()[i];
                }
            }
        }
        return role;
    }
}
