package com.github.nija123098.evelyn.economy.configs;

import com.github.nija123098.evelyn.moderation.GuildUserJoinTimeConfig;
import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.Configurable;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordUserJoin;
import com.github.nija123098.evelyn.perms.BotRole;

/**
 * Made by nija123098 on 5/1/2017.
 */
public class StartingMoneyConfig extends AbstractConfig<Integer, Configurable> {
    public StartingMoneyConfig() {
        super("starting_money", BotRole.BOT_ADMIN, 0, "The amount of money a config starts with");
    }
    @EventListener
    public void handle(DiscordUserJoin event){
        GuildUser guildUser = GuildUser.getGuildUser(event.getGuild(), event.getUser());
        if (Math.abs(event.getJoinTime() - GuildUserJoinTimeConfig.get(guildUser)) < 5000) ConfigHandler.changeSetting(CurrentMoneyConfig.class, guildUser, integer -> this.getValue(event.getGuild()));
    }// integer should be 0, but the change is for safety
}
