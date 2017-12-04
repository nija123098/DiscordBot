package com.github.nija123098.evelyn.audio.commands.current;

import com.github.nija123098.evelyn.audio.Track;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;

import static com.github.nija123098.evelyn.audio.commands.current.CurrentCommand.getPlayBar;
import static com.github.nija123098.evelyn.service.services.ScheduleService.scheduleRepeat;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class CurrentUpdateTitleCommand extends AbstractCommand {
    public CurrentUpdateTitleCommand() {
        super(CurrentCommand.class, "updatetitle", null, null, null, "updates the topic of the music channel every 10 seconds");
    }

    @Command
    public void command(GuildAudioManager manager, Channel channel) {
        scheduleRepeat(0, 10_000, () -> {
            Track track;
            if ((track = manager.currentTrack()) != null) {
                Long length = track.getLength();
                if (length == null) channel.changeTopic("Stream: " + track.getName());
                else
                    channel.changeTopic(getPlayBar(manager.isPaused(), manager.currentTime(), length) + " " + manager.currentTrack().getName());
            }
        });
    }
}
