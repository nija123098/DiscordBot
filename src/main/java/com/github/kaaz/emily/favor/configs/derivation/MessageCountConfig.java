package com.github.kaaz.emily.favor.configs.derivation;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.config.GuildUser;
import com.github.kaaz.emily.discordobjects.wrappers.event.EventListener;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordMessageDelete;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordMessageReceived;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 8/10/2017.
 */
public class MessageCountConfig extends AbstractConfig<Integer, GuildUser> {
    public MessageCountConfig() {
        super("message_count", BotRole.SYSTEM, 0, "The number of messages a user has made");
    }
    @EventListener
    public void handle(DiscordMessageReceived event){
        if (event.getMessage().getGuild() == null) return;
        this.changeSetting(GuildUser.getGuildUser(event.getMessage().getGuild(), event.getMessage().getAuthor()), integer -> ++integer);
    }
    @EventListener
    public void handle(DiscordMessageDelete event){
        if (event.getMessage().getGuild() == null) return;
        this.changeSetting(GuildUser.getGuildUser(event.getMessage().getGuild(), event.getMessage().getAuthor()), integer -> --integer);
    }
    public boolean checkDefault(){
        return false;
    }
}
