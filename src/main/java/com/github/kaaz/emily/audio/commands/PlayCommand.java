package com.github.kaaz.emily.audio.commands;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.anotations.Argument;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.github.kaaz.emily.discordobjects.wrappers.Track;
import com.github.kaaz.emily.util.SCUtil;
import com.github.kaaz.emily.util.YTUtil;

/**
 * Made by nija123098 on 6/8/2017.
 */
public class PlayCommand extends AbstractCommand {
    public PlayCommand() {
        super("play", ModuleLevel.MUSIC, null, null, "PLays music from youtube or soundcloud");
    }
    @Command
    public void command(GuildAudioManager manager, @Argument(info = "a song name or url") String s, MessageMaker maker) {
        String id = YTUtil.extractVideoCode(s);
        if (id != null) {
            manager.queueTrack(Track.getTrack(Track.Platform.YOUTUBE, id));
            return;
        }
        id = SCUtil.extractCode(s);
        if (id != null){
            manager.queueTrack(Track.getTrack(Track.Platform.SOUNDCLOUD, id));
            return;
        }
        maker.append("Unable to find video, search and playlists are not currently supported");// TODO
    }
}
