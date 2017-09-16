package com.github.nija123098.evelyn.exeption;

import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordPermission;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.perms.BotRole;

import java.util.EnumSet;

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

    public static void checkPermissions(Channel channel, User user, DiscordPermission...permissions){
        if (channel.isPrivate());//todo
        EnumSet<DiscordPermission> perm = user.getPermissionsForGuild(channel.getGuild());
        
    }
    public static void checkPermissions(Channel channel, DiscordPermission...permissions){
        checkPermissions(channel, DiscordClient.getOurUser(), permissions);
    }
}
