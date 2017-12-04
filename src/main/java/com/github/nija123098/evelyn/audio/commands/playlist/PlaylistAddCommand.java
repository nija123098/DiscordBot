package com.github.nija123098.evelyn.audio.commands.playlist;

import com.github.nija123098.evelyn.audio.Playlist;
import com.github.nija123098.evelyn.audio.Track;
import com.github.nija123098.evelyn.audio.configs.playlist.PlaylistContentsConfig;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.command.annotations.Context;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.exception.DevelopmentException;

import java.util.List;

import static com.github.nija123098.evelyn.audio.Track.getTracks;
import static com.github.nija123098.evelyn.command.ContextType.NONE;
import static com.github.nija123098.evelyn.config.ConfigHandler.alterSetting;
import static com.github.nija123098.evelyn.config.ConfigHandler.getSetting;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class PlaylistAddCommand extends AbstractCommand {
    public PlaylistAddCommand() {
        super(PlaylistCommand.class, "add", null, null, null, "Adds a track playlist, by default the current track and playlist");
    }

    @Command
    public void command(@Argument(optional = true) Playlist playlist, @Argument(optional = true, replacement = NONE) Track track, @Context(softFail = true) Track current, @Argument(optional = true, replacement = NONE) Playlist importingPlaylist, User user, Guild guild, MessageMaker maker, @Argument(optional = true, info = "A playlist url") String args) {
        Track finalTrack = track != null ? track : current;
        playlist.checkPermissionToEdit(user, guild);
        alterSetting(PlaylistContentsConfig.class, playlist, ids -> {
            if (args != null && !args.isEmpty()) {
                List<Track> tracks = getTracks(args);
                if (finalTrack != null) ids.addAll(tracks);
                else maker.append("This url doesn't point to a supported playlist");
            } else if (importingPlaylist != null)
                ids.addAll(getSetting(PlaylistContentsConfig.class, importingPlaylist));
            else if (finalTrack != null) {
                if (ids.contains(finalTrack)) {
                    maker.append("This track is already in the playlist");
                    return;
                }
                ids.add(finalTrack);
            } else throw new DevelopmentException("Choose a track, url, or playlist to import");
        });
    }
}
