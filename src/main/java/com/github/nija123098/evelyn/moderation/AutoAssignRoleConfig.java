package com.github.nija123098.evelyn.moderation;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.Role;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordUserJoin;
import com.github.nija123098.evelyn.exception.ArgumentException;

/**
 * Made by nija123098 on 6/19/2017.
 */
public class AutoAssignRoleConfig extends AbstractConfig<Role, Guild> {
    public AutoAssignRoleConfig() {
        super("role_on_join", "Role On Join", ConfigCategory.MODERATION, (Role) null, "The role to assign a new user on join");
    }
    @EventListener
    public void handle(DiscordUserJoin event){
        Role role = ConfigHandler.getSetting(AutoAssignRoleConfig.class, event.getGuild());
        if (role != null) event.getUser().addRole(role);
    }
    @Override
    protected Role validateInput(Guild configurable, Role role) {
        if (role.getPosition() > DiscordClient.getOurUser().getRolesForGuild(configurable).get(0).getPosition()) throw new ArgumentException("For me to assign roles I must have a higher role");
        return role;
    }
}
