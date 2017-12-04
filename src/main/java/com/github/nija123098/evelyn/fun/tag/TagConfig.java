package com.github.nija123098.evelyn.fun.tag;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordUserLeave;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.github.nija123098.evelyn.config.ConfigCategory.STAT_TRACKING;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class TagConfig extends AbstractConfig<Map<String, Tag>, Guild> {
    public TagConfig() {
        super("guild_tags", "", STAT_TRACKING, new ConcurrentHashMap<>(), "The tags on a guild");
    }

    @EventListener
    public void leave(DiscordUserLeave leave) {
        deleteOfUser(leave.getUser(), leave.getGuild());
    }

    static boolean deleteOfUser(User user, Guild guild) {
        AtomicBoolean deleted = new AtomicBoolean();
        ConfigHandler.alterSetting(TagConfig.class, guild, map -> map.forEach((s, tag) -> {
            if (tag.getUserID().equals(user.getID())) {
                map.remove(s);
                deleted.set(true);
            }
        }));
        return deleted.get();
    }
}
