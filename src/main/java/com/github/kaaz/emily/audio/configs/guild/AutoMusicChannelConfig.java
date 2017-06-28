package com.github.kaaz.emily.audio.configs.guild;

import com.github.kaaz.emily.audio.commands.JoinCommand;
import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.github.kaaz.emily.discordobjects.wrappers.Channel;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.event.EventListener;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordVoiceJoin;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 6/6/2017.
 */
public class AutoMusicChannelConfig extends AbstractConfig<Channel, Guild> {
    public AutoMusicChannelConfig() {
        super("auto_channel", BotRole.GUILD_TRUSTEE, null, "Sets a channel where music starts playing on join");
    }
    @EventListener
    public void listen(DiscordVoiceJoin event){
        if (event.getChannel().equals(this.getValue(event.getGuild())) && event.getGuild().getConnectedVoiceChannel() == null){
            JoinCommand.command(GuildAudioManager.getManager(event.getChannel(), true), event.getChannel(), new MessageMaker(event.getChannel()));
        }
    }
}
