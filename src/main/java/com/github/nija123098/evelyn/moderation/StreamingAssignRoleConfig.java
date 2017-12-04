package com.github.nija123098.evelyn.moderation;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.Role;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordPresenceUpdate;
import com.github.nija123098.evelyn.exception.ArgumentException;

import java.util.HashSet;
import java.util.Set;

import static com.github.nija123098.evelyn.config.ConfigCategory.MODERATION;
import static com.github.nija123098.evelyn.config.ConfigHandler.getSetting;
import static com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient.getOurUser;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class StreamingAssignRoleConfig extends AbstractConfig<Role, Guild> {
    public StreamingAssignRoleConfig() {
        super("streaming_role", "Streamer Role", MODERATION, (Role) null, "The role to assign a streaming user");
    }

    @EventListener
    public void handle(DiscordPresenceUpdate event) {
        if (event.getNewPresence().getOptionalStreamingUrl().isPresent()) {
            rolesForGuilds(event.getUser().getGuilds()).forEach(role -> {
                try {
                    event.getUser().addRole(role);
                } catch (Exception ignored) {
                }// todo role order moved
            });
        } else {
            rolesForGuilds(event.getUser().getGuilds()).forEach(role -> {
                try {
                    event.getUser().removeRole(role);
                } catch (Exception ignored) {
                }// more role order moved here
            });
        }
    }

    private static Set<Role> rolesForGuilds(Set<Guild> guilds) {
        Set<Role> roles = new HashSet<>(guilds.size() / 8);
        guilds.forEach(guild -> {
            Role role = getSetting(StreamingAssignRoleConfig.class, guild);
            if (role != null) roles.add(role);
        });
        return roles;
    }

    @Override
    protected Role validateInput(Guild configurable, Role role) {
        if (role.getPosition() < getOurUser().getRolesForGuild(configurable).get(0).getPosition())
            throw new ArgumentException("For me to assign roles I must have a higher role");
        return role;
    }
}
