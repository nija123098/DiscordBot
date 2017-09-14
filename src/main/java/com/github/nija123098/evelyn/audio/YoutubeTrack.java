package com.github.nija123098.evelyn.audio;

import com.github.nija123098.evelyn.util.CallBuffer;
import com.github.nija123098.evelyn.util.Log;
import com.github.nija123098.evelyn.util.StringHelper;
import com.github.nija123098.evelyn.util.ThreadProvider;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Made by nija123098 on 6/10/2017.
 */
public class YoutubeTrack extends DownloadableTrack {
    private static final CallBuffer CALL_BUFFER = new CallBuffer(500);
    static {
        registerTrackType(YoutubeTrack.class, YoutubeTrack::new, YoutubeTrack::new);
    }
    private final transient AtomicReference<String> name = new AtomicReference<>();
    private final transient AtomicBoolean isLiveStream = new AtomicBoolean(true);
    public YoutubeTrack(String id) {
        super(id);
        AtomicReference<Runnable> liveSet = new AtomicReference<>(), getting = new AtomicReference<>();
        liveSet.set(() -> {
            try{this.isLiveStream.set(Jsoup.connect(this.getSource()).get().head().getElementsByTag("script").toString().contains("LIVESTREAMING_DEFAULT_BROADCAST"));
            } catch (IOException e) {
                Log.log("Exception getting if a track is a stream, rescheduling: " + this.getCode(), e);
                CALL_BUFFER.call(liveSet.get());
            }
        });
        getting.set(() -> {
            try{this.name.set(((JSONObject) new JSONParser().parse(StringHelper.readAll("https://www.youtube.com/oembed?url=http%3A//youtube.com/watch%3Fv%3D" + this.getCode()))).get("title").toString());
            }catch(ParseException | IOException | UnirestException e) {
                Log.log("Exception getting name from Youtube track, rescheduling", e);
                CALL_BUFFER.call(getting.get());
            }
        });
        CALL_BUFFER.call(liveSet.get());
        CALL_BUFFER.call(getting.get());// there is probably a better way to do this
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
    @Override
    public boolean download(){
        return this.isDownloaded() && !this.isLiveStream.get() && this.actualDownload();
    }
}
