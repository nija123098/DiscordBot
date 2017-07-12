package com.github.kaaz.emily.audio;

/**
 * Made by nija123098 on 6/10/2017.
 */
public class SoundCloudTrack extends DownloadableTrack {
    static {
        Track.registerTrackType(SoundCloudTrack.class, SoundCloudTrack::new, s -> new SoundCloudTrack(toID(s)));
    }
    public SoundCloudTrack(String id) {
        super(id.contains("/") ? toID(id) : id);
    }
    protected SoundCloudTrack() {}
    public String getSource() {
        return "https://soundcloud.com/" + this.getCode();
    }
    @Override
    public String previewURL() {
        return null;
    }
    @Override
    public String getCode() {
        return toCode(getSpecificID());
    }
    private static String toID(String s){
        return s.replace("/", "_");
    }
    public static String toCode(String s){
        return s.replace("_", "/");
    }
    public String getPreferredType(){
        return "mp3";
    }
}
