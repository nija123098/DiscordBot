package com.github.kaaz.emily.audio;

import com.github.kaaz.emily.util.StringHelper;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;

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
    @Override
    public String getName() {
        try{return ((JSONObject) new JSONParser().parse(StringHelper.readAll("https://www.youtube.com/oembed?url=http%3A//youtube.com/watch%3Fv%3D" + this.getCode()))).get("title").toString();
        }catch(ParseException | IOException | UnirestException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getSource() {
        return "https://www.youtube.com/watch?v=" + this.getCode();
    }
    @Override
    public String getInfo() {
        return null;
    }
    @Override
    public String getPreviewURL() {
        return "https://i3.ytimg.com/vi/" + this.getCode() + "/0.jpg";
    }
}
