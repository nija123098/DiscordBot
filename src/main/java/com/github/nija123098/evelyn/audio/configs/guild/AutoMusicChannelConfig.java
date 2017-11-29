package com.github.nija123098.evelyn.audio.configs.guild;

import com.github.nija123098.evelyn.audio.commands.JoinCommand;
import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.VoiceChannel;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordVoiceJoin;

/**
 * Made by nija123098 on 6/6/2017.
 */
public class AutoMusicChannelConfig extends AbstractConfig<VoiceChannel, Guild> {
    public AutoMusicChannelConfig() {
        super("auto_channel", "Auto Join Channel", ConfigCategory.MUSIC, (VoiceChannel) null, "Sets a channel where music starts playing on join");
    }
    @EventListener
    public void listen(DiscordVoiceJoin event){
        if (event.getChannel().equals(this.getValue(event.getGuild())) && event.getGuild().getConnectedVoiceChannel() == null){
            JoinCommand.command(GuildAudioManager.getManager(event.getChannel(), true), event.getChannel(), new MessageMaker(event.getChannel()));
        }
    }
}
