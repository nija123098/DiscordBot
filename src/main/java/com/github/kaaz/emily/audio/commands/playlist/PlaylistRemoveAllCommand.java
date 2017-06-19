package com.github.kaaz.emily.audio.commands.playlist;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.command.anotations.Argument;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.audio.Playlist;
import com.github.kaaz.emily.audio.configs.playlist.PlaylistContentsConfig;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.User;

import java.util.ArrayList;

/**
 * Made by nija123098 on 4/14/2017.
 */
public class PlaylistRemoveAllCommand extends AbstractCommand {
    public PlaylistRemoveAllCommand() {
        super(PlaylistCommand.class, "all", null, null, null, "Removes all songs from the current playlist");
    }
    @Command
    public void command(@Argument(optional = true) Playlist playlist, User user, Guild guild, MessageMaker maker){
        playlist.checkPermissionToEdit(user, guild);
        ConfigHandler.setSetting(PlaylistContentsConfig.class, playlist, new ArrayList<>(0));
        maker.withOK();
    }
}
