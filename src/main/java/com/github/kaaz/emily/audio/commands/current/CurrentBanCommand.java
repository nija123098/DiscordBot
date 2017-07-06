package com.github.kaaz.emily.audio.commands.current;

import com.github.kaaz.emily.audio.Track;
import com.github.kaaz.emily.audio.configs.track.BannedTrackConfig;
import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.perms.BotRole;

/**
 * Made by nija123098 on 7/4/2017.
 */
public class CurrentBanCommand extends AbstractCommand {
    public CurrentBanCommand() {
        super(CurrentCommand.class, "ban", null, null, null, "Bans a track from the global playlist");
    }
    @Override
    public BotRole getBotRole() {
        return BotRole.BOT_ADMIN;
    }
    @Command
    public void command(Track track, MessageMaker maker){
        BannedTrackConfig.ban(track);
    }
}
