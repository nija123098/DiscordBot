package com.github.kaaz.emily.audio.commands.playlist;

import com.github.kaaz.emily.audio.Playlist;
import com.github.kaaz.emily.audio.Track;
import com.github.kaaz.emily.audio.configs.playlist.PlaylistContentsConfig;
import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ContextType;
import com.github.kaaz.emily.command.annotations.Argument;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.exeption.DevelopmentException;

import java.util.List;

/**
 * Made by nija123098 on 4/14/2017.
 */
public class PlaylistAddCommand extends AbstractCommand {
    public PlaylistAddCommand() {
        super(PlaylistCommand.class, "add", null, null, null, "Adds a track playlist, by default the current track and playlist");
    }
    @Command
    public void command(@Argument(optional = true) Playlist playlist, @Argument(optional = true) Track track, @Argument(optional = true, replacement = ContextType.NONE) Playlist importingPlaylist, User user, Guild guild, MessageMaker maker, @Argument(optional = true, info = "A playlist url") String args){
        playlist.checkPermissionToEdit(user, guild);
        ConfigHandler.alterSetting(PlaylistContentsConfig.class, playlist, ids -> {
            if (args != null){
                List<Track> tracks = Track.getTracks(args);
                if (track != null) ids.addAll(tracks);
                else maker.append("This url doesn't point to a supported playlist");
            }else if (importingPlaylist != null) ids.addAll(ConfigHandler.getSetting(PlaylistContentsConfig.class, importingPlaylist));
            else if (track != null){
                if (ids.contains(track)){
                    maker.append("This track is already in the playlist");
                    return;
                }
                ids.add(track);
            } else throw new DevelopmentException("Choose a track, url, or playlist to import");
        });
    }
}
