package com.github.kaaz.emily.favor.configs;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.config.ConfigLevel;
import com.github.kaaz.emily.config.Configurable;
import com.github.kaaz.emily.config.GuildUser;
import com.github.kaaz.emily.config.configs.FavorConfig;
import com.github.kaaz.emily.discordobjects.wrappers.Role;
import com.github.kaaz.emily.discordobjects.wrappers.event.EventListener;
import com.github.kaaz.emily.discordobjects.wrappers.event.botevents.ConfigValueChangeEvent;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 5/6/2017.
 */
public class EarnRankConfig extends AbstractConfig<Float, Role> {
    public EarnRankConfig() {
        super("favor_earn_amount", BotRole.GUILD_TRUSTEE, null, "A map of roles earned by users due to their favor in a guild");
    }
    @EventListener
    public void handle(ConfigValueChangeEvent<Float, Configurable> event){// change to config change event
        if (!event.getConfigurable().getConfigLevel().equals(ConfigLevel.GUILD_USER) || event.getConfigType().equals(FavorConfig.class)) return;
        GuildUser user = (GuildUser) event.getConfigurable();
        user.getGuild().getRoles().stream().filter(role -> this.getValue(role) != null).forEach(role -> {
            if (this.getValue(role) < event.getNewValue()) user.getUser().removeRole(role);
            else user.getUser().addRole(role);
        });
    }
}
