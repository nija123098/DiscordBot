package com.github.kaaz.emily.perms;

import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.Role;
import com.github.kaaz.emily.discordobjects.wrappers.event.EventListener;
import com.github.kaaz.emily.discordobjects.wrappers.event.botevents.DiscordDataReload;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordUserRolesUpdateEvent;
import com.github.kaaz.emily.launcher.BotConfig;

/**
 * Made by nija123098 on 5/15/2017.
 */
public class ContributorMonitor {
    private static final Role CONTRIB_SIGN_ROLE = Role.getRole(BotConfig.CONTRIBUTOR_SIGN_ROLE);
    private static final Role SUPPORT_SIGN_ROLE = Role.getRole(BotConfig.SUPPORTER_SIGN_ROLE);
    public static void init(){

    }
    @EventListener
    public static void handle(DiscordDataReload reload){
        Role role = Role.getRole(BotConfig.CONTRIBUTOR_SIGN_ROLE);
        if (role == null) return;
        Guild guild = role.getGuild();
        guild.getUsers().forEach(user -> {
            if (user.getRolesForGuild(guild).contains(role)) {
                if (BotRole.SUPPORTER.hasRole(user, null)) BotRole.setRole(BotRole.SUPPORTER, true, user, null);
            } else if (BotRole.SUPPORTER.hasRole(user, null)) BotRole.setRole(BotRole.SUPPORTER, false, user, null);
        });
    }
    @EventListener
    public static void handle(DiscordUserRolesUpdateEvent event){
        if (event.newRoles().contains(CONTRIB_SIGN_ROLE) && !event.oldRoles().contains(CONTRIB_SIGN_ROLE)) BotRole.setRole(BotRole.CONTRIBUTOR, true, event.getUser(), null);
        else if (!event.newRoles().contains(CONTRIB_SIGN_ROLE) && event.oldRoles().contains(CONTRIB_SIGN_ROLE)) BotRole.setRole(BotRole.CONTRIBUTOR, false, event.getUser(), null);
        if (event.newRoles().contains(SUPPORT_SIGN_ROLE) && !event.oldRoles().contains(SUPPORT_SIGN_ROLE)) BotRole.setRole(BotRole.SUPPORTER, true, event.getUser(), null);
        else if (!event.newRoles().contains(SUPPORT_SIGN_ROLE) && event.oldRoles().contains(SUPPORT_SIGN_ROLE)) BotRole.setRole(BotRole.SUPPORTER, false, event.getUser(), null);
    }
}
