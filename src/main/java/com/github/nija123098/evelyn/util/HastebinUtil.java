package com.github.nija123098.evelyn.util;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * @author nija123098
 */
public class HastebinUtil {
    /**
     * Sends a string to be hosted on hastebin.com.
     *
     * @param message the text to send to hastebind.
     * @return the key to find the post.
     */
    public static String postToHastebin(String message) {
        try{return "https://hastebin.com/" + Unirest.post("https://hastebin.com/documents").body(message).asString().getBody().substring(8, 18);
        } catch (UnirestException e) {
            Log.log("Failed posting to hastebin", e);
        }
        return "Pastebin posting failed";
    }
}
