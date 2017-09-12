package com.github.nija123098.evelyn.favor.configs.derivation;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient;
import com.github.nija123098.evelyn.perms.BotRole;
import com.github.nija123098.evelyn.service.services.ScheduleService;

public class VoiceTimeConfig extends AbstractConfig<Integer, GuildUser> {
    public VoiceTimeConfig() {
        super("voice_time", BotRole.SYSTEM, 0, "The time a user has spent in voice channel in 5min increments");
        ScheduleService.scheduleRepeat(300_000, 300_000, () -> DiscordClient.getGuilds().forEach(guild -> guild.getVoiceChannels().forEach(voiceChannel -> voiceChannel.getConnectedUsers().forEach(user -> this.alterSetting(GuildUser.getGuildUser(guild, user), integer -> ++integer)))));
    }
}
