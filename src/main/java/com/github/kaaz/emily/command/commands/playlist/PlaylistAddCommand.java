package com.github.kaaz.emily.command.commands.playlist;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.Playlist;
import com.github.kaaz.emily.config.configs.playlist.PlaylistContentsConfig;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.Track;
import com.github.kaaz.emily.discordobjects.wrappers.User;

import java.util.List;

/**
 * Made by nija123098 on 4/14/2017.
 */
public class PlaylistAddCommand extends AbstractCommand {
    public PlaylistAddCommand(PlaylistCommand command) {
        super(command, "add", null, null, null);
    }
    @Command
    public void command(User user, Guild guild, Playlist playlist, Track track, MessageMaker maker){
        playlist.checkPermissionToEdit(user, guild);
        List<String> ids = ConfigHandler.getSetting(PlaylistContentsConfig.class, playlist);
        if (ids.contains(track.getID())){
            maker.append("This track is already in the playlist");
            return;
        }
        ids.add(track.getID());
        ConfigHandler.setSetting(PlaylistContentsConfig.class, playlist, ids);
        maker.withOK();
    }
}
