package com.github.nija123098.evelyn.economy.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordUserJoin;
import com.github.nija123098.evelyn.moderation.GuildUserJoinTimeConfig;

/**
 * Made by nija123098 on 5/1/2017.
 */
public class StartingCurrencyConfig extends AbstractConfig<Integer, User> {
    public StartingCurrencyConfig() {
        super("starting_currency", ConfigCategory.ECONOMY, 0, "The amount of currency a config starts with");
    }
    @EventListener
    public void handle(DiscordUserJoin event){
        GuildUser guildUser = GuildUser.getGuildUser(event.getGuild(), event.getUser());
        if (Math.abs(event.getJoinTime() - GuildUserJoinTimeConfig.get(guildUser)) < 5000) ConfigHandler.setSetting(CurrentCurrencyConfig.class, guildUser.getUser(), 0);
    }// integer should be 0, but the change is for safety
}
