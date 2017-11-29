package com.github.nija123098.evelyn.audio.commands.playlist;

import com.github.nija123098.evelyn.audio.Playlist;
import com.github.nija123098.evelyn.audio.Track;
import com.github.nija123098.evelyn.audio.configs.playlist.PlaylistContentsConfig;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

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
