package com.github.nija123098.evelyn.exception;

import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordPermission;
import com.github.nija123098.evelyn.perms.BotRole;
import sx.blah.discord.util.MissingPermissionsException;

import java.awt.*;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

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

    public PermissionsException(MissingPermissionsException e){
        super(e);
    }

    public static void checkPermissions(Channel channel, DiscordPermission...permissions){
        if (channel.isPrivate()) return;
        EnumSet<DiscordPermission> perm = DiscordClient.getOurUser().getPermissionsForGuild(channel.getGuild());
        Set<DiscordPermission> required = new HashSet<>();
        Collections.addAll(required, permissions);
        required.removeAll(perm);
        if (!required.isEmpty()) throw new PermissionsException("I am missing the " + required + " permission to perform this action");
    }

    @Override
    public MessageMaker makeMessage(Channel channel) {
        return super.makeMessage(channel).withColor(new Color(255, 183, 76)).getTitle().clear().appendRaw("Missing Permission").getMaker();
    }
}
