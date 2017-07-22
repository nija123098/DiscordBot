package com.github.kaaz.emily.audio;

import com.github.kaaz.emily.util.AudioHelper;
import com.github.kaaz.emily.util.LangString;
import com.github.kaaz.emily.util.SpeechHelper;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import java.io.File;

/**
 * Made by nija123098 on 6/12/2017.
 */
public class SpeechTrack extends Track {
    private final File file;
    public SpeechTrack(File file) {
        this.file = file;
    }
    public SpeechTrack(LangString langString, String lang){
        this(SpeechHelper.getFile(langString, lang));
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
    public AudioTrack getTrack() {
        return AudioHelper.makeAudioTrack(this.file);
    }
    @Override
    public Long getLength() {
        return 0L;// todo?
    }
}
