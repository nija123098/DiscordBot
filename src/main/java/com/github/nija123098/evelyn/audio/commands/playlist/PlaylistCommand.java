package com.github.nija123098.evelyn.audio.commands.playlist;

import com.github.nija123098.evelyn.audio.Playlist;
import com.github.nija123098.evelyn.audio.configs.playlist.PlaylistContentsConfig;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.Configurable;
import com.github.nija123098.evelyn.config.configs.guild.GuildActivePlaylistConfig;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

import static com.github.nija123098.evelyn.command.ModuleLevel.MUSIC;
import static com.github.nija123098.evelyn.config.ConfigHandler.getSetting;
import static com.github.nija123098.evelyn.config.ConfigHandler.setSetting;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class PlaylistCommand extends AbstractCommand {
    public PlaylistCommand() {
        super("playlist", MUSIC, "pl", null, "Gets information on the current playlist and sets the active playlist");
    }

    @Command
    public void play(@Argument(optional = true) Playlist playlist, String s, Guild guild, MessageMaker maker) {
        if (s.isEmpty()) maker.getExternal().append("Use `@Evelyn pl make` to make a playlist.");
        if (playlist != null) setSetting(GuildActivePlaylistConfig.class, guild, playlist);
        else playlist = getSetting(GuildActivePlaylistConfig.class, guild);
        Configurable owner = playlist.getOwner();
        maker.getTitle().appendRaw(playlist.getName()).getMaker().getHeader()
                .appendAlternate(false, "Is owned by ", (owner == null ? "Evelyn" : ("the " + (owner instanceof User ? "user " + ((User) owner).getDisplayName(guild) : "server" + owner.getName()))))
                .appendAlternate(true, "\n", "This playlist contains " + getSetting(PlaylistContentsConfig.class, playlist).size() + " tracks");
    }

    @Override
    public ModuleLevel getModule() {
        return MUSIC;
    }
}
