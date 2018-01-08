package com.github.nija123098.evelyn.audio.commands.playlist;

import com.github.nija123098.evelyn.audio.GlobalPlaylist;
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
public class PlaylistListCommand extends AbstractCommand {
    public PlaylistListCommand() {
        super(PlaylistCommand.class, "list", null, null, null, "Lists the songs on the playlist, by default the active one");
    }
    @Command
    public void command(@Argument(optional = true) Playlist playlist, User user, Guild guild, MessageMaker maker){
        if (GlobalPlaylist.GLOBAL_PLAYLIST.equals(playlist)){
            maker.append("Whoa, that is way to long to list");
        }
        List<Track> ids = ConfigHandler.getSetting(PlaylistContentsConfig.class, playlist);
        maker.getTitle().appendRaw(playlist.getName());
        ids.forEach(s -> maker.getNewListPart().appendRaw("`" + s.getID() + "` | " + s.getName()));
    }
}
