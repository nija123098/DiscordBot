package com.github.kaaz.emily.config;

import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.perms.BotRole;

/**
 * The class for the global configurable
 */
public class GlobalConfigurable implements Configurable {
    /**
     * The global configurable object
     * for access to global configs
     */
    public static final GlobalConfigurable GLOBAL = new GlobalConfigurable();

    private GlobalConfigurable(){}
    @Override
    public String getID() {
        return "GLOBAL-id";
    }
    @Override
    public String getName() {
        return "GLOBAL";
    }
    @Override
    public ConfigLevel getConfigLevel() {
        return ConfigLevel.GLOBAL;
    }

    @Override
    public void checkPermissionToEdit(User user, Guild guild) {
        BotRole.checkRequiredRole(BotRole.BOT_ADMIN, user, null);
    }
}
