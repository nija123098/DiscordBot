package com.github.nija123098.evelyn.moderation.temporarychannels;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.discordobjects.wrappers.*;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordPresenceUpdate;
import com.github.nija123098.evelyn.perms.BotRole;
import com.github.nija123098.evelyn.util.FormatHelper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

public class TemporaryGameChannelsConfig extends AbstractConfig<Integer, Guild> {
    static final Function<String, String> playTextToChannel = s -> FormatHelper.filtering(s, c -> Character.isLetterOrDigit(c) || c == ' ');
    public TemporaryGameChannelsConfig() {
        super("temp_game_users", BotRole.GUILD_TRUSTEE, 0, "Makes a channel for when people are playing the same game 0 disables it");
    }
    @Override
    protected Integer validateInput(Guild configurable, Integer integer) {
        return Math.max(0, integer);
    }
    @EventListener
    public void handle(DiscordPresenceUpdate update){
        if (!update.getNewPresence().getOptionalPlayingText().isPresent() || update.getNewPresence().getStatus() != Presence.Status.ONLINE) return;
        String playText = update.getNewPresence().getPlayingText(), reducedPlayText = playTextToChannel.apply(playText);
        int count = 1;
        for (Guild guild : update.getUser().getGuilds()){
            if (!DiscordClient.getOurUser().getPermissionsForGuild(guild).contains(DiscordPermission.MANAGE_CHANNELS) || this.getValue(guild) < 1 || guild.getVoiceChannels().stream().map(Channel::getName).filter(s -> s.equals(reducedPlayText)).count() > 1) continue;
            for (User user : guild.getUsers()){
                if (!user.getPresence().getOptionalPlayingText().isPresent()) continue;
                if (playText.equals(user.getPresence().getPlayingText()) && ++count > this.getValue(guild)) {
                    TemporaryChannelCommand.command(false, reducedPlayText, guild);
                    return;
                }
            }
        }
    }
}
