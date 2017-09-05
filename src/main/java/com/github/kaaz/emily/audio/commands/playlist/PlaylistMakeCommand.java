package com.github.kaaz.emily.audio.commands.playlist;

import com.github.kaaz.emily.audio.configs.UserPlaylistsConfig;
import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.annotations.Argument;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.discordobjects.wrappers.User;

public class PlaylistMakeCommand extends AbstractCommand {
    public PlaylistMakeCommand() {
        super(PlaylistCommand.class, "make", "create", null, null, "Makes a playlist");
    }
    @Command
    public void command(@Argument(info = "name") String s, User user){
        String st = s.toLowerCase().split(" ")[0];
        ConfigHandler.alterSetting(UserPlaylistsConfig.class, user, strings -> strings.add(st));
    }
}
