package com.github.nija123098.evelyn.audio;

import com.github.nija123098.evelyn.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.github.nija123098.evelyn.util.FormatHelper;
import com.github.nija123098.evelyn.util.LangString;
import com.github.nija123098.evelyn.util.SpeechHelper;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import java.io.File;

/**
 * Made by nija123098 on 6/12/2017.
 */
public class SpeechTrack extends Track {
    private final File file;
    public SpeechTrack(LangString langString, String lang){
        super(FormatHelper.filtering(langString.asBuilt(), Character::isLetterOrDigit));
        this.file = SpeechHelper.getFile(langString, lang);
    }
    @Override
    public String getSource() {
        return "Emily Herself";
    }
    @Override
    public String getPreviewURL() {
        return null;
    }
    @Override
    public String getInfo() {
        return "Emily saying stuff";
    }
    @Override
    public AudioTrack getAudioTrack(GuildAudioManager manager) {
        return DownloadableTrack.makeAudioTrack(this.file);
    }
    @Override
    public Long getLength() {
        return 0L;// it's close enough
    }
}
