package com.github.nija123098.evelyn.audio.commands.playlist;

import com.github.nija123098.evelyn.audio.configs.guild.GuildPlaylistsConfig;
import com.github.nija123098.evelyn.audio.configs.playlist.UserPlaylistsConfig;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ContextType;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.Configurable;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.util.FormatHelper;

import java.util.ArrayList;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class PlaylistOwnCommand extends AbstractCommand {
    public PlaylistOwnCommand() {
        super(PlaylistCommand.class, "own", null, null, null, "Lists the playlists a user or guild owns");
    }
    @Command
    public <T extends Configurable> void command(@Argument(optional = true) User user, @Argument(optional = true, replacement = ContextType.NONE) Guild guild, MessageMaker maker){
        T target = (T) (guild != null ? guild : user);
        maker.append(target.getName() + " owns a playlist called " + FormatHelper.getList(new ArrayList<>(guild != null ? ConfigHandler.getSetting(GuildPlaylistsConfig.class, guild) : ConfigHandler.getSetting(UserPlaylistsConfig.class, user))));
    }
}
