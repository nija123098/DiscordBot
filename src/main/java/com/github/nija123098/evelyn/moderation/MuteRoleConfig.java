package com.github.nija123098.evelyn.moderation;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.Role;

import static com.github.nija123098.evelyn.config.ConfigCategory.MODERATION;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class MuteRoleConfig extends AbstractConfig<Role, Guild> {
    public MuteRoleConfig() {
        super("mute_role", "Mute Role", MODERATION, guild -> guild.getRoles().stream().filter(role -> role.getName().toLowerCase().contains("mute")).findFirst().orElse(null), "The role members who are muted will be assigned");
    }
}
