package com.github.kaaz.discordbot.perms;

import com.github.kaaz.discordbot.config.Configurable;
import com.github.kaaz.discordbot.discordwrapperobjects.Guild;
import com.github.kaaz.discordbot.discordwrapperobjects.User;
import com.github.kaaz.discordbot.util.ConfigHelper;
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

        return false;
    }
    private static BotRole getStandardBotRole(User user, Guild guild){
        if (user.isBot()){
            return BOT;
        }
        if (guild != null){
            guild.getOwner().equals(user);
        }
        return USER;
    }
    private static boolean hasFlagRank(BotRole target, User user, Guild guild){
        if (target.isGlobalFlag){
            return user.getMulitConfig("global_flag_ranks").contains(target.name().toLowerCase());
        } else if (target.isGuildFlag){
            return Configurable.getGuildUser(guild, user).getMulitConfig("guild_flag_ranks").contains(target.name().toLowerCase());
        } else {
            Log.log("Tried checking a non-flag rank for having the flag");
            return false;
        }
    }
    public static BotRole getBestBotRole(User user, Guild guild){
        return null;// TODO TEMP
    }
}
