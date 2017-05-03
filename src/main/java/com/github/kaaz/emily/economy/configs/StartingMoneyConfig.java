package com.github.kaaz.emily.economy.configs;

import com.github.kaaz.emily.automoderation.GuildUserJoinTimeConfig;
import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.Configurable;
import com.github.kaaz.emily.config.GuildUser;
import com.github.kaaz.emily.discordobjects.wrappers.event.EventListener;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordUserJoin;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 5/1/2017.
 */
public class StartingMoneyConfig extends AbstractConfig<Integer, Configurable> {
    public StartingMoneyConfig() {
        super("starting_money", BotRole.BOT_ADMIN, 0, "The amount of money a config starts with");
    }
    @EventListener
    public void handle(DiscordUserJoin join){
        GuildUser guildUser = GuildUser.getGuildUser(join.getGuild(), join.getUser());
        if (Math.abs(join.getJoinTime() - ConfigHandler.getSetting(GuildUserJoinTimeConfig.class, guildUser)) > 5000) return;
        ConfigHandler.changeSetting(CurrentMoneyConfig.class, guildUser, integer -> this.getValue(join.getGuild()));
    }// integer should be 0, but the change is for safety
}
