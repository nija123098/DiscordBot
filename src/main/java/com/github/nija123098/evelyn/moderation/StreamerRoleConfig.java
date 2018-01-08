package com.github.nija123098.evelyn.moderation;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.Role;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordPresenceUpdate;

/**
 * Made by nija123098 on 6/29/2017.
 */
public class StreamerRoleConfig extends AbstractConfig<Role, Guild> {
    public StreamerRoleConfig() {
        super("current_money", "streamer_role", ConfigCategory.GUILD_PERSONALIZATION, (Role) null, "The role always assigned to streamers, when they open a stream");
    }
    @EventListener
    public void handle(DiscordPresenceUpdate update){
        if (update.getNewPresence().getOptionalStreamingUrl().isPresent()){
            update.getUser().getGuilds().forEach(guild -> {
                Role role = this.getValue(guild);
                if (role != null && !role.getGuild().getRolesForUser(update.getUser()).contains(role)) update.getUser().addRole(role);
            });
        }
    }
}
