package com.github.kaaz.emily.audio;

import com.github.kaaz.emily.audio.configs.guild.SkipPercentConfig;
import com.github.kaaz.emily.audio.configs.guild.AutoMusicChannelConfig;
import com.github.kaaz.emily.audio.configs.guild.MusicOutputTextChannelConfig;
import com.github.kaaz.emily.audio.configs.guild.QueueTrackOnlyConfig;
import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.configs.guild.GuildActivePlaylistConfig;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.helpers.guildaudiomanager.VolumeConfig;
import com.github.kaaz.emily.discordobjects.wrappers.Channel;

/**
 * Made by nija123098 on 6/6/2017.
 */
public class MusicCommand extends AbstractCommand {
    public MusicCommand() {
        super("music", ModuleLevel.MUSIC, null, null, "Shows music related settings");
    }
    @Command
    public void command(MessageMaker maker, Channel channel){
        maker.getTitle().append("Music Configuration");
        maker.append("These are music related configs, this is just an overview.\nTo play music do `@Emily play`");
        maker.getNewFieldPart().getTitle().append("volume").getFieldPart().getValue().appendRaw(ConfigHandler.getSetting(VolumeConfig.class, channel.getGuild()) + "%");
        maker.getNewFieldPart().getTitle().append("current playlist").getFieldPart().getValue().appendRaw(ConfigHandler.getSetting(GuildActivePlaylistConfig.class, channel.getGuild()).getName());
        maker.getNewFieldPart().getTitle().append("queue type").getFieldPart().getValue().appendRaw(ConfigHandler.getSetting(QueueTrackOnlyConfig.class, channel.getGuild()) ? "Music is only played when queued" : "Playlist music is played when the queue runs out");
        maker.getNewFieldPart().getTitle().append("skip percent required").getFieldPart().getValue().appendRaw(ConfigHandler.getSetting(SkipPercentConfig.class, channel.getGuild()) + "%");
        Channel chan = ConfigHandler.getSetting(AutoMusicChannelConfig.class, channel.getGuild());
        maker.getNewFieldPart().getTitle().append("auto channel").getFieldPart().getValue().appendRaw(chan == null ? "not set" : chan.mention());
        chan = ConfigHandler.getSetting(MusicOutputTextChannelConfig.class, channel.getGuild());
        maker.getNewFieldPart().getTitle().append("track info channel").getFieldPart().getValue().appendRaw(chan == null ? "not set" : chan.mention());
    }
}
