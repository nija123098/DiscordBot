package com.github.nija123098.evelyn.audio.commands.playlist;

import com.github.nija123098.evelyn.audio.Playlist;
import com.github.nija123098.evelyn.audio.configs.playlist.PlaylistPlayTypeConfig;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ContextType;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;

import java.util.Arrays;

public class PlaylistPlayTypeCommand extends AbstractCommand {
    public PlaylistPlayTypeCommand() {
        super(PlaylistCommand.class, "playtype", null, null, null, "Shows or sets the play type of a playlist");
    }
    @Command
    public void command(@Argument(optional = true, replacement = ContextType.NONE) Playlist.PlayType playType, @Argument Playlist playlist, MessageMaker maker){
        if (playType != null) ConfigHandler.setSetting(PlaylistPlayTypeConfig.class, playlist, playType);
        else playType = ConfigHandler.getSetting(PlaylistPlayTypeConfig.class, playlist);
        maker.append("Current setting is ").appendRaw(playType.name());
        maker.append("Options are ").appendRaw(Arrays.toString(Playlist.PlayType.values()));
    }
}
