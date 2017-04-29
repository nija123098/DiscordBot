package com.github.kaaz.emily.audio.commands;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.command.anotations.Convert;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.audio.Playlist;
import com.github.kaaz.emily.audio.configs.playlist.PlaylistContentsConfig;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.Track;
import com.github.kaaz.emily.discordobjects.wrappers.User;

import java.util.List;

/**
 * Made by nija123098 on 4/14/2017.
 */
public class PlaylistAddCommand extends AbstractCommand {
    public PlaylistAddCommand() {
        super(PlaylistCommand.class, "add", null, null, null, "Adds a track playlist, by default the current track and playlist");
    }
    @Command
    public void command(@Convert(optional = true) Playlist playlist, @Convert(optional = true) Track track, User user, Guild guild, MessageMaker maker){
        playlist.checkPermissionToEdit(user, guild);
        List<Track> ids = ConfigHandler.getSetting(PlaylistContentsConfig.class, playlist);
        if (ids.contains(track)){
            maker.append("This track is already in the playlist");
            return;
        }
        ids.add(track);
        ConfigHandler.setSetting(PlaylistContentsConfig.class, playlist, ids);
        maker.withOK();
    }
}
