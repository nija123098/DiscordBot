package com.github.kaaz.emily.favor.configs.derivation;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.config.GuildUser;
import com.github.kaaz.emily.discordobjects.wrappers.event.EventListener;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordReactionEvent;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 8/10/2017.
 */
public class ReactionCountConfig extends AbstractConfig<Integer, GuildUser> {
    public ReactionCountConfig() {
        super("reaction_count", BotRole.SYSTEM, 0, "The number of reactions a user has made");
    }
    @EventListener
    public void handle(DiscordReactionEvent event){
        if (event.getMessage().getGuild() == null) return;
        this.changeSetting(GuildUser.getGuildUser(event.getMessage().getGuild(), event.getMessage().getAuthor()), integer -> integer + (event.addingReaction() ? 1 : -1));
    }
    public boolean checkDefault(){
        return false;
    }
}
