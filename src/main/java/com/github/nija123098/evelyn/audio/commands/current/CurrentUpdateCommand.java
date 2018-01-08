package com.github.nija123098.evelyn.audio.commands.current;

import com.github.nija123098.evelyn.audio.Track;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.github.nija123098.evelyn.service.services.ScheduleService;
import com.github.nija123098.evelyn.util.EmoticonHelper;

/**
 * Made by nija123098 on 5/31/2017.
 */
public class CurrentUpdateCommand extends AbstractCommand {
    public CurrentUpdateCommand() {
        super(CurrentCommand.class, "update", null, null, null, "Updates a now playing message every ten seconds");
    }
    @Command
    public void command(GuildAudioManager manager, MessageMaker maker){
        ScheduleService.scheduleRepeat(0, 10_000, () -> {
            Track track;
            if ((track = manager.currentTrack()) != null){
                Long length = track.getLength();
                maker.forceCompile().appendRaw(EmoticonHelper.getChars("notes", true) + track.getName() + (length != null ? "\n" + CurrentCommand.getPlayBar(manager.isPaused(), manager.currentTime(), length) : ""));
            }
        });
    }
}
