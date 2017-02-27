package com.github.kaaz.discordbot.perms;

import com.github.kaaz.discordbot.config.ConfigHandler;
import com.github.kaaz.discordbot.config.Configurable;
import com.github.kaaz.discordbot.config.configs.guilduser.GuildFlagRankConfig;
import com.github.kaaz.discordbot.config.configs.user.GlobalBotRoleFlagConfig;
import com.github.kaaz.discordbot.discordwrapperobjects.DiscordPermission;
import com.github.kaaz.discordbot.discordwrapperobjects.Guild;
import com.github.kaaz.discordbot.discordwrapperobjects.User;
import com.github.kaaz.discordbot.util.ConfigHelper;
import com.github.kaaz.discordbot.util.Holder;
import com.github.kaaz.discordbot.util.Log;

/**
 * Made by nija123098 on 2/20/2017.
 */
public enum BotRole {
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
        if (ConfigHelper.getValue("bot owner id").equals(user.getID())){
            return true;
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
            if (guild.getPermissionsForGuild(user).contains(DiscordPermission.ADMINISTRATOR)){
                return GUILD_ADMIN;
            }
        }
        return USER;
    }
    private static boolean hasFlagRank(BotRole target, User user, Guild guild){
        if (target.isGlobalFlag){
            return ConfigHandler.getSetting(GlobalBotRoleFlagConfig.class, user, new Holder<>()).contains(target.name().toLowerCase());
        } else if (target.isGuildFlag){
            return Configurable.getGuildUser(guild, user).getSetting(GuildFlagRankConfig.class).contains(target.name().toLowerCase());
        } else {
            Log.log("Tried checking a non-flag rank for having the flag");
            return false;
        }
    }
    public static BotRole getBestBotRole(User user, Guild guild){
        BotRole role = getStandardBotRole(user, guild);
        for (int i = role.ordinal(); i < values().length; i++) {
            if (values()[i].isFlagRank()){
                hasFlagRank(values()[i], user, guild);
            }
        }
        return role;
    }
}
