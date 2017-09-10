package com.github.nija123098.evelyn.util;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * @author Kaaz
 */
public class HastebinUtil {
    /**
     * dumps a string to hastebin
     *
     * @param message the text to send
     * @return key how to find it
     */
    public static String handleHastebin(String message) {
        try{return "https://hastebin.com/" + Unirest.post("https://hastebin.com/documents").body(message).asJson().getBody().getObject().getString("key");
        } catch (UnirestException e) {
            Log.log("Failed posting to hastebin", e);
        }
        return "Pastebin posting fail";
    }
}
