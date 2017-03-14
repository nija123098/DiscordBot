package com.github.kaaz.emily.discordobjects.exception;

import com.github.kaaz.emily.discordobjects.wrappers.DiscordPermission;
import sx.blah.discord.util.MissingPermissionsException;

import java.util.EnumSet;
import java.util.StringJoiner;

/**
 * Made by nija123098 on 3/8/2017.
 */
public class MissingPermException extends RuntimeException {
    MissingPermException(MissingPermissionsException e){
        super(e);
    }
    public EnumSet<DiscordPermission> getMissingPermission() {
        return DiscordPermission.getDiscordPermissions(((MissingPermissionsException) this.getCause()).getMissingPermissions());
    }
    public String getErrorMessage() {
        EnumSet<DiscordPermission> missing = getMissingPermission();
        if (missing == null)
            return getLocalizedMessage();
        return getMessage(missing);
    }
    private static String getMessage(EnumSet<DiscordPermission> permissions) {
        StringJoiner joiner = new StringJoiner(", ");
        permissions.stream()
                .map(Enum::name)
                .forEach(joiner::add);
        return "Missing permissions: " + joiner.toString() + "!";
    }
}
