package com.github.nija123098.evelyn.favor.configs.derivation;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.discordobjects.wrappers.Presence;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordPresenceUpdate;
import com.github.nija123098.evelyn.moderation.linkedgames.GuildLinkedGamesConfig;
import com.github.nija123098.evelyn.perms.BotRole;
import com.github.nija123098.evelyn.util.FormatHelper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayTimeFavorConfig extends AbstractConfig<Integer, GuildUser> {
    private Map<User, Long> map = new ConcurrentHashMap<>();
    public PlayTimeFavorConfig() {
        super("play_time", BotRole.SYSTEM, 0, "The time a user has spent in a game which is approved for favor game in 5 min increments");
    }
    @EventListener
    public void handle(DiscordPresenceUpdate update){
        long current = System.currentTimeMillis();
        if (map.containsKey(update.getUser()) && isValidPresence(update.getOldPresence())){
            String reduced = FormatHelper.reduce(update.getOldPresence().getOptionalPlayingText().get());
            update.getUser().getGuilds().forEach(guild -> {
                if (FormatHelper.reduce(ConfigHandler.getSetting(GuildLinkedGamesConfig.class, guild)).contains(reduced)){
                    this.changeSetting(GuildUser.getGuildUser(guild, update.getUser()), integer -> integer + (int) (map.get(update.getUser()) - current) / 300_000);
                }
            });
            map.remove(update.getUser());
        }
        if (isValidPresence(update.getNewPresence())) map.put(update.getUser(), System.currentTimeMillis());
    }
    private boolean isValidPresence(Presence presence){
        return presence.getOptionalPlayingText().isPresent() && presence.getStatus() != Presence.Status.DND && presence.getStatus() != Presence.Status.IDLE;
    }
}
