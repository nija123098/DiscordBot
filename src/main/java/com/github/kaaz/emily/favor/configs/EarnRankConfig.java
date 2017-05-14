package com.github.kaaz.emily.favor.configs;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.config.ConfigLevel;
import com.github.kaaz.emily.config.GuildUser;
import com.github.kaaz.emily.config.configs.FavorConfig;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.Role;
import com.github.kaaz.emily.discordobjects.wrappers.event.EventListener;
import com.github.kaaz.emily.discordobjects.wrappers.event.botevents.ConfigValueChangeEvent;
import com.github.kaaz.emily.perms.BotRole;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Made by nija123098 on 5/6/2017.
 */
public class EarnRankConfig extends AbstractConfig<Map<Float, Role>, Guild> {
    public EarnRankConfig() {
        super("favor_earn_rank", BotRole.GUILD_TRUSTEE, new HashMap<>(), "A map of roles earned by users due to their favor in a guild");
    }
    @EventListener
    public void handle(ConfigValueChangeEvent event){// change to config change event
        if (!event.getConfigType().equals(FavorConfig.class) || event.getConfigurable().getConfigLevel() != ConfigLevel.GUILD_USER) return;
        GuildUser guildUser = ((GuildUser) event.getConfigurable());
        float value = (float) event.getNewValue();
        Set<Role> has = new HashSet<>(guildUser.getGuild().getRolesForUser(guildUser.getUser()));
        this.getValue(guildUser.getGuild()).forEach((flo, role) -> {
            if (value >= flo && !has.contains(role)) guildUser.getUser().addRole(role);
            else if (value < flo && has.contains(role)) guildUser.getUser().removeRole(role);
        });
    }
}
