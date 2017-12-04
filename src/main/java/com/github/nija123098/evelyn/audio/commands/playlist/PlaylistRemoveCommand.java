package com.github.nija123098.evelyn.audio.commands.playlist;

import com.github.nija123098.evelyn.audio.Playlist;
import com.github.nija123098.evelyn.audio.Track;
import com.github.nija123098.evelyn.audio.configs.playlist.PlaylistContentsConfig;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

import java.util.List;

import static com.github.nija123098.evelyn.config.ConfigHandler.getSetting;
import static com.github.nija123098.evelyn.config.ConfigHandler.setSetting;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class PlaylistRemoveCommand extends AbstractCommand {
    public PlaylistRemoveCommand() {
        super(PlaylistCommand.class, "remove", null, null, null, "Removes a track from the current playlist, by default the current playing track");
    }

    @Command
    public void command(@Argument(optional = true) Playlist playlist, @Argument(optional = true) Track track, User user, Guild guild, MessageMaker maker) {
        playlist.checkPermissionToEdit(user, guild);
        List<Track> ids = getSetting(PlaylistContentsConfig.class, playlist);
        if (!ids.contains(track)) {
            maker.append("This track is not in the playlist");
            return;
        }
        ids.remove(track);
        setSetting(PlaylistContentsConfig.class, playlist, ids);
    }
}
