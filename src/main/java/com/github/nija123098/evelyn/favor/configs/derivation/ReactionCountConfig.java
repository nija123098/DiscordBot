package com.github.nija123098.evelyn.favor.configs.derivation;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordReactionEvent;
import com.github.nija123098.evelyn.favor.FavorChangeEvent;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class ReactionCountConfig extends AbstractConfig<Integer, GuildUser> {
    public ReactionCountConfig() {
        super("reaction_count", "", ConfigCategory.STAT_TRACKING, 0, "The number of reactions a user has made");
    }
    @EventListener
    public void handle(DiscordReactionEvent event) {
        if (event.getMessage() == null || event.getUser().isBot() || event.getMessage().getChannel().isPrivate()) return;
        GuildUser guildUser = GuildUser.getGuildUser(event.getMessage().getGuild(), event.getMessage().getAuthor());
        if (guildUser == null) return;// Some user was banned
        FavorChangeEvent.process(event.getUser(), () -> this.changeSetting(GuildUser.getGuildUser(event.getMessage().getGuild(), event.getMessage().getAuthor()), integer -> {
            if (integer == null) integer = 0;
            return integer + (event.addingReaction() ? 1 : -1);
        }));
    }
}
