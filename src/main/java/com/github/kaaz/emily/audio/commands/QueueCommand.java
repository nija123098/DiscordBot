package com.github.kaaz.emily.audio.commands;

import com.github.kaaz.emily.audio.configs.track.DurrationTimeConfig;
import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.github.kaaz.emily.audio.Track;
import com.github.kaaz.emily.util.Time;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Made by nija123098 on 5/24/2017.
 */
public class QueueCommand extends AbstractCommand {
    public QueueCommand() {
        super("queue", ModuleLevel.MUSIC, "q", null, "Shows the music queue");
    }
    @Command
    public void command(GuildAudioManager manager, MessageMaker maker){
        List<Track> tracks = manager.getQueue();
        if (tracks.size() == 0){
            maker.append("There are no items in the queue");
            return;
        }
        AtomicLong time = new AtomicLong();
        tracks.forEach(track -> time.addAndGet(ConfigHandler.getSetting(DurrationTimeConfig.class, track)));
        maker.append("There are " + tracks.size() + " items with a estimated play time of ").appendRaw(Time.getAbbreviated(time.get()) + "\n");
        tracks.forEach(track -> maker.getNewListPart().append(track.getID() + " | " + Time.getAbbreviatedMusic(ConfigHandler.getSetting(DurrationTimeConfig.class, track)) + " | " + track.getName()));
    }
}
