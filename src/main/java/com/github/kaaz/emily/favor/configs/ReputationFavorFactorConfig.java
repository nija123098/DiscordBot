package com.github.kaaz.emily.favor.configs;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.GuildUser;
import com.github.kaaz.emily.config.configs.FavorConfig;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.event.EventListener;
import com.github.kaaz.emily.discordobjects.wrappers.event.botevents.ConfigValueChangeEvent;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 5/1/2017.
 */
public class ReputationFavorFactorConfig extends AbstractConfig<Float, Guild> {
    public ReputationFavorFactorConfig() {
        super("reputation_favor_factor", BotRole.GUILD_TRUSTEE, 5f, "The factor by which favor is bestowed on a guild user for a reaction");
    }
    @EventListener
    public void handle(ConfigValueChangeEvent event){
        if (!event.getConfigType().equals(GuildUserReputationConfig.class)) return;
        GuildUser guildUser = (GuildUser) event.getConfigurable();
        ConfigHandler.changeSetting(FavorConfig.class, guildUser, old -> old + ((Integer) event.getNewValue() - (Integer) event.getOldValue()) * this.getValue(guildUser.getGuild()));
    }
}
