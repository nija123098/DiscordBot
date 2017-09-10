package com.github.nija123098.evelyn.audio.commands.playlist;

import com.github.nija123098.evelyn.audio.configs.UserPlaylistsConfig;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

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
