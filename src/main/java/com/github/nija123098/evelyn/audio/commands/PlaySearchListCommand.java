package com.github.nija123098.evelyn.audio.commands;

import com.github.nija123098.evelyn.audio.YoutubeTrack;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.github.nija123098.evelyn.util.EmoticonHelper;
import com.github.nija123098.evelyn.util.YTUtil;

import java.util.List;

public class PlaySearchListCommand extends AbstractCommand {
    private static final String[] NAMES = {"one", "two", "three", "four", "five"};
    public PlaySearchListCommand() {
        super(PlayCommand.class, "searchlist", "ps", null, "search, list, s", "Lists the songs it gets info on");
    }
    @Command
    public void command(@Argument String search, GuildAudioManager audioManager, MessageMaker maker) {
        List<YoutubeTrack> tracks = YTUtil.getTrack(search, 4);
        if (tracks.isEmpty()) {
            maker.append("No tracks match that.");
            return;
        }
        maker.append("Use reactions to indicate which song you would like to play!");
        for (int i = 0; i < tracks.size(); i++) {
            maker.getNewListPart().appendRaw(EmoticonHelper.getChars(NAMES[i], false) + "  ").appendEmbedLink(tracks.get(i).getName(), tracks.get(i).getSource());
            int finalI = i;
            maker.withPublicReactionBehavior(NAMES[i], (add, reaction, user) -> {
                audioManager.queueTrack(tracks.get(finalI));
                maker.clearReactionBehaviors();
            });
        }
    }
}
