package com.github.nija123098.evelyn.fun.tag;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordUserLeave;
import com.github.nija123098.evelyn.perms.BotRole;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Made by nija123098 on 5/30/2017.
 */
public class TagConfig extends AbstractConfig<Map<String, Tag>, Guild> {
    public TagConfig() {
        super("guild_tags", BotRole.GUILD_TRUSTEE, new ConcurrentHashMap<>(), "The tags on a guild");
    }
    @EventListener
    public void leave(DiscordUserLeave leave){
        deleteOfUser(leave.getUser(), leave.getGuild());
    }
    static boolean deleteOfUser(User user, Guild guild){
        AtomicBoolean deleted = new AtomicBoolean();
        ConfigHandler.alterSetting(TagConfig.class, guild, map -> map.forEach((s, tag) -> {
            if (tag.getUserID().equals(user.getID())) {
                map.remove(s);
                deleted.set(true);
            }
        }));
        return deleted.get();
    }
    public boolean checkDefault(){
        return false;
    }
}
