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

import static com.github.nija123098.evelyn.audio.GlobalPlaylist.GLOBAL_PLAYLIST;
import static com.github.nija123098.evelyn.config.ConfigHandler.getSetting;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class PlaylistListCommand extends AbstractCommand {
    public PlaylistListCommand() {
        super(PlaylistCommand.class, "list", null, null, null, "Lists the songs on the playlist, by default the active one");
    }

    @Command
    public void command(@Argument(optional = true) Playlist playlist, User user, Guild guild, MessageMaker maker) {
        if (GLOBAL_PLAYLIST.equals(playlist)) {
            maker.append("Whoa, that is way to long to list");
        }
        List<Track> ids = getSetting(PlaylistContentsConfig.class, playlist);
        maker.getTitle().appendRaw(playlist.getName());
        ids.forEach(s -> maker.getNewListPart().appendRaw("`" + s.getID() + "` | " + s.getName()));
    }
}
