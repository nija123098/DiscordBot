package com.github.kaaz.emily.command.commands.playlist;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.Playlist;
import com.github.kaaz.emily.config.configs.playlist.PlaylistContentsConfig;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.User;

import java.util.ArrayList;

/**
 * Made by nija123098 on 4/14/2017.
 */
public class PlaylistRemoveAllCommand extends AbstractCommand {
    public PlaylistRemoveAllCommand(PlaylistCommand command) {
        super(command, "all", null, null, null);
    }
    @Command
    public void command(User user, Guild guild, Playlist playlist, MessageMaker maker){
        playlist.checkPermissionToEdit(user, guild);
        ConfigHandler.setSetting(PlaylistContentsConfig.class, playlist, new ArrayList<>(0));
        maker.withOK();
    }
}
