package com.github.nija123098.evelyn.audio.commands.playlist;

import com.github.nija123098.evelyn.audio.Playlist;
import com.github.nija123098.evelyn.audio.configs.playlist.PlaylistContentsConfig;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

import java.util.ArrayList;

/**
 * Made by nija123098 on 4/14/2017.
 */
public class PlaylistRemoveAllCommand extends AbstractCommand {
    public PlaylistRemoveAllCommand() {
        super(PlaylistRemoveCommand.class, "all", null, null, null, "Removes all songs from the current playlist");
    }
    @Command
    public void command(@Argument(optional = true) Playlist playlist, User user, Guild guild){
        playlist.checkPermissionToEdit(user, guild);
        ConfigHandler.setSetting(PlaylistContentsConfig.class, playlist, new ArrayList<>(0));
    }
}
