package com.github.nija123098.evelyn.helping;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.discordobjects.wrappers.VoiceChannel;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordVoiceJoin;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordVoiceLeave;
import com.github.nija123098.evelyn.service.services.MemoryManagementService;
import com.github.nija123098.evelyn.util.LangString;

import java.util.List;

/**
 * Made by nija123098 on 7/20/2017.
 */
public class HeraldConfig extends AbstractConfig<Boolean, Guild> {
    public HeraldConfig() {
        super("herald_active", "herald", ConfigCategory.GUILD_PERSONALIZATION, true, "Makes the bot announce the join and leave of users");
    }
    @EventListener
    public void handle(DiscordVoiceJoin event){
        herald(event.getChannel(), event.getUser(), true);
    }
    @EventListener
    public void handle(DiscordVoiceLeave event){
        herald(event.getChannel(), event.getUser(), false);
    }
    private static final List<GuildUser> BUFFER = new MemoryManagementService.ManagedList<>(30_000);
    private void herald(VoiceChannel channel, User user, boolean join){
        if (DiscordClient.getOurUser().equals(user)) return;
        GuildAudioManager manager = getManager(channel);
        if (manager == null) return;
        GuildUser guildUser = GuildUser.getGuildUser(channel.getGuild(), user);
        if (!BUFFER.contains(guildUser)) manager.interrupt(new LangString(false, user.getDisplayName(channel.getGuild()) + " ").append(true, "has " + (join ? "joined" : "left") + " the channel"));
        BUFFER.add(guildUser);
    }
    private GuildAudioManager getManager(VoiceChannel channel){
        GuildAudioManager manager = GuildAudioManager.getManager(channel, false);
        return manager != null && getValue(channel.getGuild()) ? manager : null;
    }
}
