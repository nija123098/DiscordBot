package com.github.nija123098.evelyn.audio;

import com.github.nija123098.evelyn.audio.configs.guild.SkipPercentConfig;
import com.github.nija123098.evelyn.audio.configs.guild.AutoMusicChannelConfig;
import com.github.nija123098.evelyn.audio.configs.guild.MusicOutputTextChannelConfig;
import com.github.nija123098.evelyn.audio.configs.guild.QueueTrackOnlyConfig;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.configs.guild.GuildActivePlaylistConfig;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.helpers.guildaudiomanager.VolumeConfig;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;

/**
 * Made by nija123098 on 6/6/2017.
 */
public class MusicCommand extends AbstractCommand {
    public MusicCommand() {
        super("music", ModuleLevel.MUSIC, null, null, "Shows music related settings");
    }
    @Command
    public void command(MessageMaker maker, Guild guild){
        maker.getTitle().append("Music Configuration");
        maker.append("These are music related configs, this is just an overview.\nTo play music do `@Emily play`");
        maker.getNewFieldPart().getTitle().append("volume").getFieldPart().getValue().appendRaw(ConfigHandler.getSetting(VolumeConfig.class, guild) + "%");
        maker.getNewFieldPart().getTitle().append("current playlist").getFieldPart().getValue().appendRaw(ConfigHandler.getSetting(GuildActivePlaylistConfig.class, guild).getName());
        maker.getNewFieldPart().getTitle().append("queue type").getFieldPart().getValue().appendRaw(ConfigHandler.getSetting(QueueTrackOnlyConfig.class, guild) ? "Music is only played when queued" : "Playlist music is played when the queue runs out");
        maker.getNewFieldPart().getTitle().append("skip percent required").getFieldPart().getValue().appendRaw(ConfigHandler.getSetting(SkipPercentConfig.class, guild) + "%");
        Channel chan = ConfigHandler.getSetting(AutoMusicChannelConfig.class, guild);
        maker.getNewFieldPart().getTitle().append("auto channel").getFieldPart().getValue().appendRaw(chan == null ? "not set" : chan.mention());
        chan = ConfigHandler.getSetting(MusicOutputTextChannelConfig.class, guild);
        maker.getNewFieldPart().getTitle().append("track info channel").getFieldPart().getValue().appendRaw(chan == null ? "not set" : chan.mention());
    }
}
