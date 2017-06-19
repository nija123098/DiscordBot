package com.github.kaaz.emily.util;

import com.github.kaaz.emily.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.sedmelluq.discord.lavaplayer.source.local.LocalAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.track.AudioReference;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import java.io.File;

/**
 * Made by nija123098 on 6/9/2017.
 */
public class AudioHelper {
    private static final LocalAudioSourceManager LOCAL_SOURCE_MANAGER = new LocalAudioSourceManager();
    public static AudioTrack makeAudioTrack(File file){
        if (file == null) return null;
        return (AudioTrack) LOCAL_SOURCE_MANAGER.loadItem(GuildAudioManager.PLAYER_MANAGER, new AudioReference(file.getAbsolutePath(), null));
    }
}
