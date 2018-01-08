package com.github.nija123098.evelyn.audio.commands.playlist;

import com.github.nija123098.evelyn.audio.configs.guild.GuildPlaylistsConfig;
import com.github.nija123098.evelyn.audio.configs.playlist.UserPlaylistsConfig;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ContextType;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.Configurable;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.exeption.ArgumentException;
import com.github.nija123098.evelyn.util.FormatHelper;

import java.util.Set;

public class PlaylistMakeCommand extends AbstractCommand {
    public PlaylistMakeCommand() {
        super(PlaylistCommand.class, "make", "create", null, null, "Makes a playlist");
    }
    @Command
    public <T extends Configurable, V extends Set<String>> void command(@Argument(optional = true, info = "is server playlist", replacement = ContextType.NONE) Boolean guildPlaylist, @Argument(info = "name") String s, User user, Guild guild){
        if (guildPlaylist == null) guildPlaylist = false;
        if (s.isEmpty()) throw new ArgumentException("Your playlist must have a name");
        if (!s.equals(FormatHelper.filtering(s, Character::isLetter))) throw new ArgumentException("A playlist name must only contain letters");
        ConfigHandler.alterSetting((Class<? extends AbstractConfig<V, T>>) (guildPlaylist ? GuildPlaylistsConfig.class : UserPlaylistsConfig.class), (T) (guildPlaylist ? guild : user), strings -> strings.add(s));
    }
}
