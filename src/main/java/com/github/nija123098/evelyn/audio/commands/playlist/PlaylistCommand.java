package com.github.nija123098.evelyn.audio.commands.playlist;

import com.github.nija123098.evelyn.audio.Playlist;
import com.github.nija123098.evelyn.audio.configs.playlist.PlaylistContentsConfig;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.Configurable;
import com.github.nija123098.evelyn.config.configs.guild.GuildActivePlaylistConfig;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

/**
 * Made by nija123098 on 4/13/2017.
 */
public class PlaylistCommand extends AbstractCommand {
    public PlaylistCommand() {
        super("playlist", ModuleLevel.MUSIC, "pl", null, "Gets information on the current playlist and sets the active playlist");
    }
    @Command
    public void play(@Argument(optional = true) Playlist playlist, String s, Guild guild, MessageMaker maker){
        if (s.isEmpty()) maker.getExternal().append("Use `@Evelyn pl make` to make a playlist.");
        if (playlist != null) ConfigHandler.setSetting(GuildActivePlaylistConfig.class, guild, playlist);
        else playlist = ConfigHandler.getSetting(GuildActivePlaylistConfig.class, guild);
        Configurable owner = playlist.getOwner();
        maker.getTitle().appendRaw(playlist.getName()).getMaker().getHeader()
                .appendAlternate(false, "Is owned by ", (owner == null ? "Evelyn" : ("the " + (owner instanceof User ? "user " + ((User) owner).getDisplayName(guild) : "server" + owner.getName()))))
                .appendAlternate(true, "\n", "This playlist contains " + ConfigHandler.getSetting(PlaylistContentsConfig.class, playlist).size() + " tracks");
    }
    @Override
    public ModuleLevel getModule(){
        return ModuleLevel.MUSIC;
    }
}
