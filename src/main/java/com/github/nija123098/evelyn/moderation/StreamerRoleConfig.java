package com.github.nija123098.evelyn.moderation;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.Role;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordPresenceUpdate;

import static com.github.nija123098.evelyn.config.ConfigCategory.GUILD_PERSONALIZATION;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class StreamerRoleConfig extends AbstractConfig<Role, Guild> {
    public StreamerRoleConfig() {
        super("streamer_role", "Auto Streamer Role", GUILD_PERSONALIZATION, (Role) null, "The role always assigned to streamers, when they open a stream");
    }

    @EventListener
    public void handle(DiscordPresenceUpdate update) {
        if (update.getNewPresence().getOptionalStreamingUrl().isPresent()) {
            update.getUser().getGuilds().forEach(guild -> {
                Role role = this.getValue(guild);
                if (role != null && !role.getGuild().getRolesForUser(update.getUser()).contains(role))
                    update.getUser().addRole(role);
            });
        }
    }
}
