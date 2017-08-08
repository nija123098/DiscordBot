package com.github.kaaz.emily.util;

import com.github.kaaz.emily.launcher.BotConfig;
import ga.dryco.redditjerk.implementation.RedditApi;
import ga.dryco.redditjerk.wrappers.Link;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Made by nija123098 on 6/4/2017.
 */
public class RedditLink {
    private static final Map<String, LinkOrigin> LINK_ORIGINS;
    static {
        RedditApi.getRedditInstance(BotConfig.USER_AGENT);
        LINK_ORIGINS = new HashMap<>(LinkOrigin.values().length);
        for (LinkOrigin origin : LinkOrigin.values()) LINK_ORIGINS.put(origin.URL, origin);
    }//"imgur.com", "i.reddituploads.com", "youtube.com"
    private String url, title, content, fileURL, fileType, pointerUrl;
    private File file;
    private boolean linkApproved;
    public RedditLink(Link link) {
        this.url = "https://www.reddit.com/" + link.getPermalink();
        this.title = link.getTitle();
        this.content = link.getSelftext();
        if (this.content.isEmpty()) this.content = null;
        this.pointerUrl = link.getUrl();
        this.fileURL = !link.getUrl().contains("www.reddit.com") ? link.getUrl() : null;
        if (this.fileURL != null){
            LinkOrigin approved = LINK_ORIGINS.get(this.fileURL.split("/")[2]);
            this.linkApproved = approved != null;
            if (this.linkApproved && approved.downloadSupported){
                this.fileURL = approved.sourceFunction.apply(this.fileURL);
                this.fileType = StringHelper.getFileType(this.fileURL);
            }
        }
    }
    public String getUrl() {
        return this.url;
    }
    public String getTitle() {
        return this.title;
    }
    public String getContent() {
        return this.content;
    }
    public String getFileURL() {
        return this.fileURL;
    }
    public String getFileType() {
        return this.fileType;
    }
    public String getPointerUrl() {
        return this.pointerUrl;
    }
    public boolean getLinkApproved(){
        return this.linkApproved;
    }
    public File getFile() {
        if (this.file == null && this.fileType != null){
            StringBuilder builder = new StringBuilder();
            new StringIterator(this.title).forEachRemaining(character -> builder.append(Character.isLetterOrDigit(character) ? character : '_'));
            try (InputStream in = new URL(this.fileURL).openStream()) {
                File outputfile = FileHelper.getTempFile("redditimages", this.fileType, builder.toString());
                outputfile.createNewFile();
                outputfile.deleteOnExit();
                IOUtils.copy(in, new FileOutputStream(outputfile));
                this.file = outputfile;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return this.file;
    }
    public enum LinkOrigin {
        IMGER("imgur.com"),
        YOUTUBE("www.youtube.com"),
        REDDIT_UPLOADS("i.reddituploads.com"),
        GFYCAT("gfycat.com"),
        THUMBS_GFYCAT("thumbs.gfycat.com"),
        I_IMGER("i.imgur.com", String::toString),
        REDDIT("i.redd.it", String::toString),;
        private String URL;
        private Function<String, String> sourceFunction;
        private boolean downloadSupported;
        LinkOrigin(String URL){
            this.URL = URL;
            this.sourceFunction = String::toString;
            this.downloadSupported = false;
        }
        LinkOrigin(String URL, Function<String, String> sourceFunction) {
            this.URL = URL;
            this.sourceFunction = sourceFunction;
            this.downloadSupported = true;
        }
    }
}
