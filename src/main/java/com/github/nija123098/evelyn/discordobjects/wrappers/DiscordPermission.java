package com.github.nija123098.evelyn.discordobjects.wrappers;

import sx.blah.discord.handle.obj.Permissions;

import java.util.Collection;
import java.util.EnumSet;

/**
 * Made by nija123098 on 2/23/2017.
 */
public enum DiscordPermission {
    CREATE_INVITE,
    KICK,
    BAN,
    ADMINISTRATOR,
    MANAGE_CHANNELS,
    MANAGE_CHANNEL,
    MANAGE_SERVER,
    ADD_REACTIONS,
    VIEW_AUDIT_LOG,
    READ_MESSAGES,
    SEND_MESSAGES,
    SEND_TTS_MESSAGES,
    MANAGE_MESSAGES,
    EMBED_LINKS,
    ATTACH_FILES,
    READ_MESSAGE_HISTORY,
    MENTION_EVERYONE,
    USE_EXTERNAL_EMOJIS,
    VOICE_CONNECT,
    VOICE_SPEAK,
    VOICE_MUTE_MEMBERS,
    VOICE_DEAFEN_MEMBERS,
    VOICE_MOVE_MEMBERS,
    VOICE_USE_VAD,
    CHANGE_NICKNAME,
    MANAGE_NICKNAMES,
    MANAGE_ROLES,
    MANAGE_PERMISSIONS,
    MANAGE_WEBHOOKS,
    MANAGE_EMOJIS,;
    public boolean hasPermission(User user, Guild guild){
        return user.getPermissionsForGuild(guild).contains(this);
    }
    public static DiscordPermission getDiscordPermissions(Permissions permissions){
        return values()[permissions.ordinal()];
    }
    public static EnumSet<DiscordPermission> getDiscordPermissions(Collection<Permissions> permissions){
        EnumSet<DiscordPermission> list = EnumSet.noneOf(DiscordPermission.class);
        permissions.forEach(perm -> list.add(getDiscordPermissions(perm)));
        return list;
    }
    public static Permissions getPermission(DiscordPermission permission) {
        return Permissions.values()[permission.ordinal()];
    }
    public static EnumSet<Permissions> getPermissions(Collection<DiscordPermission> permissions){
        EnumSet<Permissions> list = EnumSet.noneOf(Permissions.class);
        permissions.forEach(discordPermission -> list.add(getPermission(discordPermission)));
        return list;
    }
}
