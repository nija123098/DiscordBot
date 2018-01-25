package com.github.nija123098.evelyn.exception;

import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordPermission;
import com.github.nija123098.evelyn.perms.BotRole;
import sx.blah.discord.util.MissingPermissionsException;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class PermissionsException extends UserIssueException {
    public PermissionsException() {
        this("You do not have sufficient permission to do that");
    }

    public PermissionsException(String message) {
        super(message);
    }

    public PermissionsException(BotRole role) {
        this("You must be at least a " + role.name() + " to do that");
    }

    public PermissionsException(MissingPermissionsException e) {
        super(e);
    }

    public static void checkPermissions(Channel channel, DiscordPermission... permissions) {
        if (channel.isPrivate()) return;
        EnumSet<DiscordPermission> perm = DiscordClient.getOurUser().getPermissionsForGuild(channel.getGuild());
        Set<DiscordPermission> required = new HashSet<>();
        Collections.addAll(required, permissions);
        required.removeAll(perm);
        if (!required.isEmpty()) throw new PermissionsException("I am missing the " + required + " permission to perform this action");
    }
}
