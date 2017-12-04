package com.github.nija123098.evelyn.audio.commands.playlist;

import com.github.nija123098.evelyn.audio.Playlist;
import com.github.nija123098.evelyn.audio.configs.playlist.PlaylistContentsConfig;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

import java.util.ArrayList;

import static com.github.nija123098.evelyn.config.ConfigHandler.setSetting;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class PlaylistRemoveAllCommand extends AbstractCommand {
    public PlaylistRemoveAllCommand() {
        super(PlaylistRemoveCommand.class, "all", null, null, null, "Removes all songs from the current playlist");
    }

    @Command
    public void command(@Argument(optional = true) Playlist playlist, User user, Guild guild) {
        playlist.checkPermissionToEdit(user, guild);
        setSetting(PlaylistContentsConfig.class, playlist, new ArrayList<>(0));
    }
}
