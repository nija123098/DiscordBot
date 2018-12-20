package com.github.nija123098.evelyn.favor.configs.derivation;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordVoiceJoin;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordVoiceLeave;
import com.github.nija123098.evelyn.favor.FavorChangeEvent;
import com.github.nija123098.evelyn.launcher.Launcher;

import java.util.HashMap;
import java.util.Map;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class VoiceTimeConfig extends AbstractConfig<Long, GuildUser> {
    private static final Map<GuildUser, Long> TIME_MAP = new HashMap<>();
    public VoiceTimeConfig() {
        super("voice_channel_time", "", ConfigCategory.STAT_TRACKING, 0L, "The time a user has spent in voice channel in 5min increments");
        Launcher.registerStartup(() -> DiscordClient.getGuilds().forEach(guild -> guild.getVoiceChannels().forEach(voiceChannel -> voiceChannel.getConnectedUsers().forEach(user -> TIME_MAP.put(GuildUser.getGuildUser(voiceChannel.getGuild(), user), System.currentTimeMillis())))));
        Launcher.registerShutdown(() -> DiscordClient.getGuilds().forEach(guild -> guild.getVoiceChannels().forEach(voiceChannel -> voiceChannel.getConnectedUsers().forEach(user -> {
            GuildUser guildUser = GuildUser.getGuildUser(voiceChannel.getGuild(), user);
            Long time = TIME_MAP.remove(guildUser);
            if (time == null) return;
            FavorChangeEvent.process(guildUser, () -> this.changeSetting(guildUser, integer -> integer + time));
        }))));
    }// moves are only between channels in a single guild
    @EventListener
    public void handle(DiscordVoiceJoin join) {
        if (!join.isUnique()) return;
        TIME_MAP.put(GuildUser.getGuildUser(join.getGuild(), join.getUser()), System.currentTimeMillis());
    }
    @EventListener
    public void handle(DiscordVoiceLeave leave) {
        if (!leave.isUnique()) return;
        GuildUser guildUser = GuildUser.getGuildUser(leave.getGuild(), leave.getUser());
        if (guildUser == null) return;// kick protection
        Long time = TIME_MAP.remove(guildUser);
        if (time == null) return;
        FavorChangeEvent.process(guildUser, () -> this.changeSetting(guildUser, integer -> integer + time));
    }
}
