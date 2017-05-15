package com.github.kaaz.emily.perms;

import com.github.kaaz.emily.discordobjects.wrappers.Role;
import com.github.kaaz.emily.discordobjects.wrappers.event.EventListener;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordUserRolesUpdateEvent;
import com.github.kaaz.emily.launcher.BotConfig;

/**
 * Made by nija123098 on 5/15/2017.
 */
public class ContributorMonitor {
    private static final Role CONTRIB_SIGN_ROLE = Role.getRole(BotConfig.CONTRIBUTOR_SIGN_ROLE);
    public static void init(){}
    @EventListener
    public void handle(DiscordUserRolesUpdateEvent event){
        if (event.newRoles().contains(CONTRIB_SIGN_ROLE) && !event.oldRoles().contains(CONTRIB_SIGN_ROLE)) BotRole.setRole(BotRole.SUPPORTER, true, event.getUser(), null);
        else if (!event.newRoles().contains(CONTRIB_SIGN_ROLE) && event.oldRoles().contains(CONTRIB_SIGN_ROLE)) BotRole.setRole(BotRole.SUPPORTER, false, event.getUser(), null);
    }
}
