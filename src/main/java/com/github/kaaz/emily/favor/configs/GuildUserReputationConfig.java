package com.github.kaaz.emily.favor.configs;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.config.GuildUser;
import com.github.kaaz.emily.discordobjects.wrappers.event.EventListener;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordReactionEvent;
import com.github.kaaz.emily.perms.BotRole;
import com.github.kaaz.emily.util.EmoticonHelper;

/**
 * Made by nija123098 on 5/1/2017.
 */
public class GuildUserReputationConfig extends AbstractConfig<Integer, GuildUser> {
    private static final String POSITIVE_REP = EmoticonHelper.getChars("+1"), NEGATIVE_REP = EmoticonHelper.getChars("-1");
    public GuildUserReputationConfig() {
        super("guild_user_reputation", BotRole.GUILD_TRUSTEE, 0, "Guild based reputation for a guild member");
    }
    @EventListener
    public void handle(DiscordReactionEvent event){
        if (event.getMessage().getGuild() == null) return;
        int value = (event.getReaction().getChars().equals(POSITIVE_REP) ? 1 : event.getReaction().getChars().equals(NEGATIVE_REP) ? -1 : 0) * (event.addingReaction() ? 1 : -1);
        if (value == 0) return;
        GuildUser guildUser = GuildUser.getGuildUser(event.getMessage().getGuild(), event.getMessage().getAuthor());
        this.setValue(guildUser, value + this.getValue(guildUser));
    }
}
