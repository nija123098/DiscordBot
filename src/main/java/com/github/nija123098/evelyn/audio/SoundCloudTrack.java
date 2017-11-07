package com.github.nija123098.evelyn.audio;

/**
 * A track type representing a track from Soundcloud.
 *
 * @author nija123098
 * @since 1.0.0
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
    public String getPreviewURL() {
        return null;
    }
    @Override
    public String getInfo() {
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
