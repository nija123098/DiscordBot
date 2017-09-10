package com.github.nija123098.evelyn.helping.presence;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordPermission;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.Presence;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordMessageReceived;
import com.github.nija123098.evelyn.perms.BotRole;
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
    public void handle(DiscordMessageReceived event){
        if (event.getChannel().isPrivate() || event.getGuild().getUserSize() < 16 || event.getAuthor().isBot()) return;
        Set<String> set = event.getMessage().getMentions().stream().filter(user -> event.getChannel().getModifiedPermissions(user).contains(DiscordPermission.READ_MESSAGES)).filter(user -> user.getPresence().getStatus() != Presence.Status.ONLINE || ConfigHandler.getSetting(SelfMarkedAwayConfig.class, user)).map(user -> user.getDisplayName(event.getGuild()) + " is " + (user.getPresence().getStatus() == Presence.Status.ONLINE ? "AFK" : user.getPresence().getStatus())).collect(Collectors.toSet());
        if (set.isEmpty()) return;
        new MessageMaker(event.getMessage()).appendRaw(Joiner.on(", ").join(set)).withDeleteDelay(Math.max(5_000, set.size() * 1_500L)).send();
    }
}
