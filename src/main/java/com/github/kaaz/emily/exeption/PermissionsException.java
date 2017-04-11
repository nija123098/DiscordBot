package com.github.kaaz.emily.exeption;

import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 4/10/2017.
 */
public class PermissionsException extends BotException {
    public PermissionsException() {
        this("You do not have sufficient permission to do that");
    }

    public PermissionsException(String message) {
        super(message);
    }

    public PermissionsException(BotRole role){
        this("You must be at least a " + role.name() + " to do that");
    }
}
