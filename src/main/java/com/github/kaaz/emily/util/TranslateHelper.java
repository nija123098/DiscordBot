package com.github.kaaz.emily.util;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Made by nija123098 on 3/17/2017.
 */
public class TranslateHelper {// <lang to, <content, translated content>>, todo move to LangString
    private static final Map<String, Map<String, String>> MAP = new HashMap<>();
    private static String call(String lang, String content) {
        try {
            return MAP.computeIfAbsent(lang, s -> new HashMap<>()).computeIfAbsent(content, s -> {
                try {
                    HttpURLConnection con = (HttpURLConnection) new URL("https://translate.googleapis.com/translate_a/single?client=gtx&sl=auto&tl=" + lang + "&dt=t&q=" + URLEncoder.encode(content, "UTF-8")).openConnection();
                    con.setRequestProperty("User-Agent", "Mozilla/5.0");
                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                    String inputLine;
                    StringBuilder response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    return ((JSONArray) ((JSONArray) new JSONArray(response.toString()).get(0)).get(0)).get(0).toString();
                } catch (IOException e) {
                    throw new RuntimeException("Exception while attempting to get translation", e);
                }
            });
        } catch (RuntimeException e) {
            return content;
        }
    }
    public static String translate(String lang, String content) {
        return MAP.computeIfAbsent(lang, s -> new HashMap<>()).computeIfAbsent(content, s -> {
            String building = "";
            String[] contents = content.split("\n");
            int before = 0, after = 0;
            for (int i = 0; i < contents.length; i++) {
                before = contents[i].indexOf(" ");
                if (before == -1) {
                    before = 0;
                }
                for (int j = contents[i].length() - 1; j > -1; --j) {
                    if (contents[i].charAt(j) != ' '){
                        break;
                    }
                    ++after;
                }
                if (i != contents.length - 1){
                    building += "\n";
                }
            }
            return FormatHelper.repeat(' ', before) + call(lang, building) + FormatHelper.repeat(' ', after);
        });
    }
}
