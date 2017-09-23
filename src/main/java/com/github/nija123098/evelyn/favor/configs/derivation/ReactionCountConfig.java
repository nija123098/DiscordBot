package com.github.nija123098.evelyn.favor.configs.derivation;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordReactionEvent;
import com.github.nija123098.evelyn.favor.FavorChangeEvent;
import com.github.nija123098.evelyn.perms.BotRole;

/**
 * Made by nija123098 on 8/10/2017.
 */
public class ReactionCountConfig extends AbstractConfig<Integer, GuildUser> {
    public ReactionCountConfig() {
        super("reaction_count", ConfigCategory.STAT_TRACKING, 0, "The number of reactions a user has made");
    }
    @EventListener
    public void handle(DiscordReactionEvent event){
        if (event.getMessage().getGuild() == null) return;
        FavorChangeEvent.process(event.getUser(), () -> this.changeSetting(GuildUser.getGuildUser(event.getMessage().getGuild(), event.getMessage().getAuthor()), integer -> integer + (event.addingReaction() ? 1 : -1)));
    }
}
