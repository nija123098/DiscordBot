package com.github.kaaz.emily.audio.commands.current;

import com.github.kaaz.emily.audio.configs.track.DurrationTimeConfig;
import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.anotations.Argument;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.github.kaaz.emily.audio.Track;
import com.github.kaaz.emily.exeption.ArgumentException;
import com.github.kaaz.emily.util.Time;

/**
 * Made by nija123098 on 5/30/2017.
 */
public class CurrentSeekCommand extends AbstractCommand {
    public CurrentSeekCommand() {
        super(CurrentCommand.class, "seek", null, null, "goto", "Go to specified timestamp of track (eg 3m10s");
    }
    @Command
    public void command(@Argument Time time, GuildAudioManager manager, MessageMaker maker){
        Track track = manager.currentTrack();
        if (ConfigHandler.getSetting(DurrationTimeConfig.class, track) < time.schedualed()) throw new ArgumentException("The track is not that long");
        manager.seek(time.timeUntil());
    }
}
