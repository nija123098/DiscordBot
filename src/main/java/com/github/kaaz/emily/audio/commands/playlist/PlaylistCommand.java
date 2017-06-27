package com.github.kaaz.emily.audio.commands.playlist;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.command.annotations.Argument;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.Configurable;
import com.github.kaaz.emily.audio.Playlist;
import com.github.kaaz.emily.config.configs.guild.GuildActivePlaylistConfig;
import com.github.kaaz.emily.audio.configs.playlist.PlaylistContentsConfig;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.User;

/**
 * Made by nija123098 on 4/13/2017.
 */
public class PlaylistCommand extends AbstractCommand {
    public PlaylistCommand() {
        super("playlist", ModuleLevel.MUSIC, "pl", null, "Gets information on the current playlist");
    }
    @Command
    public void play(@Argument(optional = true) Playlist playlist, Guild guild, MessageMaker maker){
        if (playlist != null){
            ConfigHandler.setSetting(GuildActivePlaylistConfig.class, guild, playlist);
        }
        playlist = ConfigHandler.getSetting(GuildActivePlaylistConfig.class, guild);
        Configurable owner = playlist.getOwner();
        maker.getTitle().appendRaw(playlist.getName()).getMaker().getHeader()
                .appendAlternate(false, "Is owned by ", (owner == null ? "Emily" : ("the " + (owner instanceof User ? "user " + ((User) owner).getDisplayName(guild) : "guild " + ((Guild) owner).getName()))))
                .appendAlternate(true, "\n", "This playlist contains " + ConfigHandler.getSetting(PlaylistContentsConfig.class, playlist).size() + " tracks");
    }
    @Override
    public ModuleLevel getModule(){
        return ModuleLevel.MUSIC;
    }
}
