package com.github.nija123098.evelyn.favor.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.discordobjects.wrappers.Role;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.favor.FavorChangeEvent;
import com.github.nija123098.evelyn.perms.BotRole;

/**
 * Made by nija123098 on 5/6/2017.
 */
public class EarnRankConfig extends AbstractConfig<Float, Role> {
    public EarnRankConfig() {
        super("favor_requirement", BotRole.GUILD_TRUSTEE, null, "A map of roles earned by users due to their favor in a guild");
    }
    @EventListener
    public void handle(FavorChangeEvent event){// change to config change event
        if (!event.getConfigurable().getClass().equals(GuildUser.class)) return;
        GuildUser user = (GuildUser) event.getConfigurable();
        user.getGuild().getRoles().stream().filter(role -> this.getValue(role) != null).forEach(role -> {
            if (this.getValue(role) > event.getNewValue()) user.getUser().removeRole(role);
            else user.getUser().addRole(role);
        });
    }
}
