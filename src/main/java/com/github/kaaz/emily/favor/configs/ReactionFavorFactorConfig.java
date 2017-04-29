package com.github.kaaz.emily.favor.configs;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.event.EventListener;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordReactionEvent;
import com.github.kaaz.emily.favor.FavorHandler;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 4/27/2017.
 */
public class ReactionFavorFactorConfig extends AbstractConfig<Float, Guild> {
    public ReactionFavorFactorConfig() {
        super("reaction_favor_factor", BotRole.GUILD_TRUSTEE, .1F, "The factor by which favor is bestowed on a guild user for a reaction");
    }
    @EventListener
    public void handle(DiscordReactionEvent event){
        if (event.getMessage().getGuild() != null) FavorHandler.addFavorLevel(event.getUser(), (event.addingReaction() && event.getMessage().getContent().length() > 6 ? 1 : -1) * this.getValue(event.getMessage().getGuild()));
    }
}
