package com.github.kaaz.emily.automoderation;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.Role;
import com.github.kaaz.emily.discordobjects.wrappers.event.EventListener;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordPresenceUpdate;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 6/29/2017.
 */
public class StreamerRoleConfig extends AbstractConfig<Role, Guild> {
    public StreamerRoleConfig() {
        super("streamer_role", BotRole.GUILD_TRUSTEE, null, "The role always assigned to streamers, when they open a stream");
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
