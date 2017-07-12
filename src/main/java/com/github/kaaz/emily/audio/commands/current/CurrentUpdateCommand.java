package com.github.kaaz.emily.audio.commands.current;

import com.github.kaaz.emily.audio.Track;
import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.github.kaaz.emily.service.services.ScheduleService;
import com.github.kaaz.emily.util.EmoticonHelper;

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
                maker.forceCompile().appendRaw(EmoticonHelper.getChars("notes") + track.getName() + (length != null ? "\n" + CurrentCommand.getPlayBar(manager.isPaused(), manager.currentTime(), length) : ""));
            }
        });
    }
}
