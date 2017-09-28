package com.github.nija123098.evelyn.discordobjects.exception;

import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordPermission;
import com.github.nija123098.evelyn.exeption.BotException;
import sx.blah.discord.util.MissingPermissionsException;

import java.util.EnumSet;
import java.util.StringJoiner;

/**
 * Made by nija123098 on 3/8/2017.
 */
public class MissingPermException extends BotException {
    private EnumSet<DiscordPermission> missing;
    public MissingPermException(DiscordPermission permission){
        this.missing = EnumSet.of(permission);
    }
    MissingPermException(MissingPermissionsException e){
        this.missing = DiscordPermission.getDiscordPermissions(e.getMissingPermissions());
    }
    public EnumSet<DiscordPermission> getMissingPermission() {
        return this.missing;
    }
    @Override
    public String getMessage() {
        StringJoiner joiner = new StringJoiner(", ");
        this.missing.stream()
                .map(Enum::name)
                .forEach(joiner::add);
        return "Missing permissions: " + joiner.toString();
    }
}
