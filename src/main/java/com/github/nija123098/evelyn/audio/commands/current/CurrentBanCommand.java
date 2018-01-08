package com.github.nija123098.evelyn.audio.commands.current;

import com.github.nija123098.evelyn.audio.Track;
import com.github.nija123098.evelyn.audio.configs.track.BannedTrackConfig;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.perms.BotRole;

/**
 * Made by nija123098 on 7/4/2017.
 */
public class CurrentBanCommand extends AbstractCommand {
    public CurrentBanCommand() {
        super(CurrentCommand.class, "ban", "no_entry_sign", null, null, "Bans a track from the global playlist");
    }
    @Override
    public BotRole getBotRole() {
        return BotRole.BOT_ADMIN;
    }
    @Command
    public void command(Track track){
        BannedTrackConfig.ban(track);
    }
}
