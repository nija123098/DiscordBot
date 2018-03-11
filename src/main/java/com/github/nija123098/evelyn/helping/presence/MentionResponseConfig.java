package com.github.nija123098.evelyn.helping.presence;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.*;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordMessageReceived;
import com.google.api.client.repackaged.com.google.common.base.Joiner;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class MentionResponseConfig extends AbstractConfig<Boolean, Guild> {
    public MentionResponseConfig() {
        super("afk_mention_response", "Mention User Status", ConfigCategory.GUILD_PERSONALIZATION, false, "The bot responds mentioning the stasis of a user if they are marked NDN, AWAY, OFFLINE, or self marked AFK or AWAY");
    }
    @EventListener
    public void handle(DiscordMessageReceived event) {
        if (event.getChannel().isPrivate() || event.getGuild().getUserSize() < 16 || event.getAuthor().isBot() || !this.getValue(event.getGuild())) return;
        Set<User> users = event.getMessage().getMentions().stream().filter(user -> !DiscordClient.getOurUser().equals(user)).filter(user -> event.getChannel().getModifiedPermissions(user).contains(DiscordPermission.READ_MESSAGES)).filter(user -> user.getPresence().getStatus() != Presence.Status.ONLINE || ConfigHandler.getSetting(SelfMarkedAwayConfig.class, user)).collect(Collectors.toSet());
        if (users.isEmpty()) return;
        if (users.size() != 1) users.removeIf(User::isBot);
        Set<String> set = users.stream().map(user -> user.getDisplayName(event.getGuild()) + " is " + (user.getPresence().getStatus() == Presence.Status.ONLINE ? "AFK" : user.getPresence().getStatus())).collect(Collectors.toSet());
        if (set.isEmpty() || set.size() > 5) return;
        new MessageMaker(event.getMessage()).appendRaw(Joiner.on(", ").join(set)).withDeleteDelay(5_000L).send();
    }
}
