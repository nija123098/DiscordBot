package com.github.kaaz.emily.helping.presence;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.Presence;
import com.github.kaaz.emily.discordobjects.wrappers.event.EventListener;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordMessageReceivedEvent;
import com.github.kaaz.emily.perms.BotRole;
import com.google.api.client.repackaged.com.google.common.base.Joiner;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Made by nija123098 on 6/21/2017.
 */
public class MentionResponseConfig extends AbstractConfig<Boolean, Guild> {
    public MentionResponseConfig() {
        super("afk_mention_response", BotRole.GUILD_TRUSTEE, true, "The bot responds mentioning the stasis of a user if they are marked NDN, AWAY, OFFLINE, or self marked AFK or AWAY");
    }
    @EventListener
    public void handle(DiscordMessageReceivedEvent event){
        if (!BotRole.USER.hasRequiredRole(event.getAuthor(), event.getGuild())) return;
        Set<String> set = event.getMessage().getMentions().stream().filter(user -> user.getPresence().getStatus() != Presence.Status.ONLINE || ConfigHandler.getSetting(SelfMarkedAwayConfig.class, user)).map(user -> user.getDisplayName(event.getGuild()) + " is " + (user.getPresence().getStatus() == Presence.Status.ONLINE ? "AFK" : user.getPresence().getStatus())).collect(Collectors.toSet());
        if (set.isEmpty()) return;
        new MessageMaker(event.getMessage()).appendRaw(Joiner.on(", ").join(set)).withDeleteDelay(5_000L).send();
    }
}
