package com.github.nija123098.evelyn.audio.commands.playlist;

import com.github.nija123098.evelyn.audio.configs.UserPlaylistsConfig;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.exeption.ArgumentException;
import com.github.nija123098.evelyn.util.FormatHelper;

public class PlaylistMakeCommand extends AbstractCommand {
    public PlaylistMakeCommand() {
        super(PlaylistCommand.class, "make", "create", null, null, "Makes a playlist");
    }
    @Command
    public void command(@Argument(info = "name") String s, User user){
        if (s.isEmpty()) throw new ArgumentException("Your playlist must have a name");
        if (!s.equals(FormatHelper.filtering(s, Character::isLetter))) throw new ArgumentException("A playlist name must only contain letters");
        ConfigHandler.alterSetting(UserPlaylistsConfig.class, user, strings -> strings.add(s));
    }
}
