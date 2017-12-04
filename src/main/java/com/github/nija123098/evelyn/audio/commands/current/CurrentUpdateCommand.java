package com.github.nija123098.evelyn.audio.commands.current;

import com.github.nija123098.evelyn.audio.Track;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.helpers.guildaudiomanager.GuildAudioManager;

import static com.github.nija123098.evelyn.audio.commands.current.CurrentCommand.getPlayBar;
import static com.github.nija123098.evelyn.service.services.ScheduleService.scheduleRepeat;
import static com.github.nija123098.evelyn.util.EmoticonHelper.getChars;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class CurrentUpdateCommand extends AbstractCommand {
    public CurrentUpdateCommand() {
        super(CurrentCommand.class, "update", null, null, null, "Updates a now playing message every ten seconds");
    }

    @Command
    public void command(GuildAudioManager manager, MessageMaker maker) {
        scheduleRepeat(0, 10_000, () -> {
            Track track;
            if ((track = manager.currentTrack()) != null) {
                Long length = track.getLength();
                maker.forceCompile().appendRaw(getChars("notes", true) + track.getName() + (length != null ? "\n" + getPlayBar(manager.isPaused(), manager.currentTime(), length) : ""));
            }
        });
    }
}
