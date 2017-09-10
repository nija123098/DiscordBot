package com.github.nija123098.evelyn.audio;

import com.github.nija123098.evelyn.util.Log;
import com.github.nija123098.evelyn.util.StringHelper;
import com.github.nija123098.evelyn.util.ThreadProvider;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Made by nija123098 on 6/10/2017.
 */
public class YoutubeTrack extends DownloadableTrack {
    static {
        registerTrackType(YoutubeTrack.class, YoutubeTrack::new, YoutubeTrack::new);
    }
    private final transient AtomicReference<String> name = new AtomicReference<>();
    public YoutubeTrack(String id) {
        super(id);
        ThreadProvider.sub(() -> {
            try{this.name.set(((JSONObject) new JSONParser().parse(StringHelper.readAll("https://www.youtube.com/oembed?url=http%3A//youtube.com/watch%3Fv%3D" + this.getCode()))).get("title").toString());
            }catch(ParseException | IOException | UnirestException e) {
                Log.log("Exception getting name from Youtube track", e);
            }
        });
    }
    protected YoutubeTrack() {}
    @Override
    public String getName() {
        return this.name.get() != null ? this.name.get() : "I'm loading the song name as fast as I can";
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
