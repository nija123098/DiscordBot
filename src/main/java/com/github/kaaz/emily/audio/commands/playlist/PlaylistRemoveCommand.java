package com.github.kaaz.emily.audio.commands.playlist;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.command.anotations.Argument;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.audio.Playlist;
import com.github.kaaz.emily.audio.configs.playlist.PlaylistContentsConfig;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.audio.Track;
import com.github.kaaz.emily.discordobjects.wrappers.User;

import java.util.List;

/**
 * Made by nija123098 on 4/14/2017.
 */
public class PlaylistRemoveCommand extends AbstractCommand {
    public PlaylistRemoveCommand() {
        super(PlaylistCommand.class, "remove", null, null, null, "Removes a track from the current playlist, by default the current playing track");
    }
    @Command
    public void command(@Argument(optional = true) Playlist playlist, @Argument(optional = true) Track track, User user, Guild guild, MessageMaker maker){
        playlist.checkPermissionToEdit(user, guild);
        List<Track> ids = ConfigHandler.getSetting(PlaylistContentsConfig.class, playlist);
        if (!ids.contains(track)){
            maker.append("This track is not in the playlist");
            return;
        }
        ids.remove(track);
        ConfigHandler.setSetting(PlaylistContentsConfig.class, playlist, ids);
    }
}
