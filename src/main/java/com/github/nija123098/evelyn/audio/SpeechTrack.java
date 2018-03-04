package com.github.nija123098.evelyn.audio;

import com.github.nija123098.evelyn.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient;
import com.github.nija123098.evelyn.util.FormatHelper;
import com.github.nija123098.evelyn.util.LangString;
import com.github.nija123098.evelyn.util.SpeechHelper;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import java.io.File;

/**
 * The type of track for defining something
 * the bot says in a voice channel.
 *
 * @author nija123098
 * @since 1.0.0
 */
public class SpeechTrack extends Track {
    private final File file;
    public SpeechTrack(LangString langString, String lang){
        super(FormatHelper.filtering(langString.asBuilt(), Character::isLetterOrDigit));
        this.file = SpeechHelper.getFile(langString, lang);
    }
    @Override
    public String getSource() {
        return "Evelyn herself";
    }
    @Override
    public String getPreviewURL() {
        return DiscordClient.getOurUser().getAvatarURL();
    }
    @Override
    public String getInfo() {
        return "Evelyn saying stuff";
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
