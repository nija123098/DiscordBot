package com.github.nija123098.evelyn.exeption;

import com.github.nija123098.evelyn.perms.BotRole;

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
