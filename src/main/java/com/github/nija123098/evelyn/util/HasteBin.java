package com.github.nija123098.evelyn.util;

import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * An utility to post stuff to hastebin.com
 *
 * @author Celestialdeath99
 * @since 1.0.0
 */
public class HasteBin {

    /**
     * Returns a URL to the hastebin.com post
     * @param data The data to be posted to hastebin.com
     * @return Returns the URL to the hastebin.com post
     */
    public static String post(String data) {
        // Create and set the HTTP Client and its posting location
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost("https://hastebin.com/documents");

        // Post the data to hastebin and return a URL
        try {
            post.setEntity(new StringEntity(data));
            HttpResponse response = client.execute(post);
            String result = EntityUtils.toString(response.getEntity());
            return "https://hastebin.com/" + new JsonParser().parse(result).getAsJsonObject().get("key").getAsString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Returns an error message if it could not post the data
        return "Could not post!";
    }
}
