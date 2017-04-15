package com.github.kaaz.emily.command.commands.playlist;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.Playlist;
import com.github.kaaz.emily.config.configs.playlist.PlaylistContentsConfig;
import com.github.kaaz.emily.config.configs.track.TrackNameConfig;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.Track;
import com.github.kaaz.emily.discordobjects.wrappers.User;

import java.util.List;

/**
 * Made by nija123098 on 4/14/2017.
 */
public class PlaylistListCommand extends AbstractCommand {
    public PlaylistListCommand(PlaylistCommand command) {
        super(command, "list", null, null, null);
    }
    @Command
    public void command(User user, Guild guild, Playlist playlist, MessageMaker maker){
        playlist.checkPermissionToEdit(user, guild);
        if (Playlist.GLOBAL_PLAYLIST.equals(playlist)){
            maker.append("Whoa, that is way to long to list");
        }
        List<Track> ids = ConfigHandler.getSetting(PlaylistContentsConfig.class, playlist);
        maker.getTitle().appendRaw(playlist.getName());
        ids.forEach(s -> maker.getNewListPart().appendRaw("`" + s + "` | " + ConfigHandler.getSetting(TrackNameConfig.class, s)));
        maker.withOK();
    }
}
