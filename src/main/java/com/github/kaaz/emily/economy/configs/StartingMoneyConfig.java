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
public class StartingMoneyConfig extends AbstractConfig<Float, Configurable> {
    public StartingMoneyConfig() {
        super("starting_money", BotRole.BOT_ADMIN, 0F, "The amount of money a config starts with");
    }
    @EventListener
    public void handle(DiscordUserJoin event){
        GuildUser guildUser = GuildUser.getGuildUser(event.getGuild(), event.getUser());
        if (Math.abs(event.getJoinTime() - GuildUserJoinTimeConfig.get(guildUser)) < 5000)
            ConfigHandler.changeSetting(CurrentMoneyConfig.class, guildUser, integer -> this.getValue(event.getGuild()));
    }// integer should be 0, but the change is for safety
}
