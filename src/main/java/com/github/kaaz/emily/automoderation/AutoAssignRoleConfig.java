package com.github.kaaz.emily.automoderation;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.discordobjects.wrappers.DiscordClient;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.Role;
import com.github.kaaz.emily.discordobjects.wrappers.event.EventListener;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordUserJoin;
import com.github.kaaz.emily.exeption.ArgumentException;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 6/19/2017.
 */
public class AutoAssignRoleConfig extends AbstractConfig<Role, Guild>{
    public AutoAssignRoleConfig() {
        super("role_on_join", BotRole.GUILD_TRUSTEE, null, "The role to assign a new user on join");
    }
    @EventListener
    public void handle(DiscordUserJoin event){
        Role role = ConfigHandler.getSetting(AutoAssignRoleConfig.class, event.getGuild());
        if (role != null) event.getUser().addRole(role);
    }
    @Override
    protected void validateInput(Guild configurable, Role role) {
        if (role.getPosition() < DiscordClient.getOurUser().getRolesForGuild(configurable).get(0).getPosition()) throw new ArgumentException("For me to assign roles I must have a higher role");
    }
}
