package com.github.kaaz.emily.audio;

/**
 * Made by nija123098 on 6/10/2017.
 */
public class YoutubeTrack extends DownloadableTrack {
    static {
        Track.registerTrackType(YoutubeTrack.class, YoutubeTrack::new, YoutubeTrack::new);
    }
    public YoutubeTrack(String id) {
        super(id);
    }
    protected YoutubeTrack() {}
    public String getSource() {
        return "https://www.youtube.com/watch?v=" + this.getCode();
    }
}
