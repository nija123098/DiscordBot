package com.github.nija123098.evelyn.moderation.temporary;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.VoiceChannel;

public class TemporaryGameVoiceChannelConfig extends AbstractConfig<String, VoiceChannel> {// todo re-enable
    // private static final Map<Guild, Set<VoiceChannel>> GUILD_VOICE_CHANNEL_MAP = new ConcurrentHashMap<>();
    // private static final Map<VoiceChannel, Integer> INTEGER_MAP = new ConcurrentHashMap<>();
    public TemporaryGameVoiceChannelConfig() {
        super("temporary_game_voice_channel", "Temporary Game Voice Channel", ConfigCategory.STAT_TRACKING, (String) null, "Indicates the name of the game that this voice channel is a temporary channel for");
        /*
        Launcher.registerStartup(() -> this.getNonDefaultSettings().forEach((channel, s) -> {
            int count = (int) channel.getGuild().getUsers().stream().map(User::getPresence).map(Presence::getOptionalPlayingText).filter(Optional::isPresent).filter(optional -> optional.get().equals(s)).count();
            if (count < ConfigHandler.getSetting(TemporaryGameChannelsConfig.class, channel.getGuild())) TemporaryChannelConfig.attemptDelete(channel);
            else {
                INTEGER_MAP.put(channel, count);
                GUILD_VOICE_CHANNEL_MAP.computeIfAbsent(channel.getGuild(), guild -> new HashSet<>()).add(channel);
            }
        }));
        */
    }
    /*
    @EventListener
    public void handle(DiscordPresenceUpdate update) {
        if (!update.getNewPresence().getOptionalPlayingText().isPresent() && !update.getOldPresence().getOptionalPlayingText().isPresent()) return;
        update.getUser().getGuilds().stream().map(GUILD_VOICE_CHANNEL_MAP::get).filter(channels -> !channels.isEmpty()).forEach(voiceChannels -> voiceChannels.forEach(voiceChannel -> {
            if (update.getNewPresence().getOptionalPlayingText().isPresent()) {
                if (update.getNewPresence().getOptionalPlayingText().get().equals(voiceChannel.getName())) {
                    INTEGER_MAP.compute(voiceChannel, (voiceChannel1, integer) -> ++integer);
                }
            } else if (update.getOldPresence().getOptionalPlayingText().isPresent()) {
                if (update.getOldPresence().getOptionalPlayingText().get().equals(voiceChannel.getName())) {
                    INTEGER_MAP.compute(voiceChannel, (voiceChannel1, integer) -> --integer);
                    checkDelete(voiceChannel);
                }
            }
        }));
    }
    private boolean sufficient(int val, Guild guild) {
        return val >= ConfigHandler.getSetting(TemporaryGameChannelsConfig.class, guild);
    }
    private void checkDelete(VoiceChannel channel) {
        if (!sufficient(INTEGER_MAP.get(channel), channel.getGuild())) TemporaryChannelConfig.attemptDelete(channel);
    }
    */
}
/*
I'll figure this out properly later.

    private static final Map<Channel, Integer> INTEGER_MAP = new ConcurrentHashMap<>();
    private static final Map<String, Set<Channel>> CH = new ConcurrentHashMap<>();

        this.getNonDefaultSettings().forEach((channel, s) -> {
            CH.computeIfAbsent(s, st -> new HashSet<>()).add(channel);
        });
        this.getNonDefaultSettings().keySet().stream().map(Channel::getGuild).distinct().forEach(guild -> {
            guild.getUsers().stream().map(User::getPresence).map(Presence::getOptionalPlayingText).filter(Optional::isPresent).forEach(optional -> {
                Set<Channel> channels = CH.get(optional.get());
                if (channels == null) return;
                channels.stream().filter(channel -> channel.getGuild().equals(guild)).forEach(channel -> {
                    if (guild.equals(channel.getGuild())) {
                        INTEGER_MAP.compute(channel, (chan, integer) -> integer == null ? 1 : ++integer);
                    }
                });
            });
        });
        INTEGER_MAP.forEach((s, integer) -> {
            if (TemporaryGameChannelsConfig.CONFIG.get().getValue())
        });

 */
