package com.github.nija123098.evelyn.config;

import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.perms.BotRole;

/**
 * The class for the global configurable, a singleton utilized
 * for saving global {@link AbstractConfig}s using the config system.
 *
 * @author nija123098
 * @since 1.0.0
 */
public class GlobalConfigurable implements Configurable {
    /**
     * The global configurable object
     * for access to global configs.
     */
    public static final GlobalConfigurable GLOBAL = new GlobalConfigurable();

    private GlobalConfigurable(){
        this.registerExistence();
    }
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
        BotRole.BOT_ADMIN.checkRequiredRole(user, null);
    }
}
