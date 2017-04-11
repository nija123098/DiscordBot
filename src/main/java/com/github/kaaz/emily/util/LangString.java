package com.github.kaaz.emily.util;

import javafx.util.Pair;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Made by nija123098 on 3/17/2017.
 */
public class LangString {
    /**
     * The value of the lang string in pairs
     * of strings and booleans, representing
     * if that string should be translated
     */
    private final List<Pair<Boolean, String>> value = new ArrayList<>();
    private final Map<String, String> translated = new HashMap<>(1);
    public LangString() {
    }
    public LangString(boolean translate, String content) {
        append(translate, content);
    }
    public LangString append(boolean translate, String content) {
        value.add(new Pair<>(translate, content));
        return this;
    }
    public LangString appendTranslation(String content) {
        value.add(new Pair<>(true, content));
        return this;
    }
    public LangString appendRaw(String content) {
        value.add(new Pair<>(false, content));
        return this;
    }
    public LangString appendToggle(boolean rawFirst, String...content) {
        for (String c : content) {
            rawFirst = !rawFirst;
            append(rawFirst, c);
        }
        return this;
    }
    public boolean hasContents(){
        return this.value.size() > 0;
    }
    public String translate(String lang) {
        return translated.computeIfAbsent(lang, s -> {
            final StringBuilder builder = new StringBuilder();
            value.forEach(pair -> builder.append(pair.getKey() ? translate(lang, pair.getValue()) : pair.getValue()));
            return builder.toString();
        });
    }

    /**
     * Returns the value of the string
     * without translation, which should be
     * equivalent to translating the LangString
     * with the original language
     *
     * @return the value without any translation
     */
    public String asBuilt() {
        final StringBuilder builder = new StringBuilder();
        value.forEach(pair -> builder.append(pair.getValue()));
        return builder.toString();
    }

    /**
     * The storage for API calls for translated content
     */
    private static final Map<String, Map<String, String>> MAP = new HashMap<>();

    /**
     * Translates the content, but unlike
     * {@link LangString#call(String, String)}
     * it formats the calls properly
     *
     * @param lang the lang code to translate to
     * @param content the content to translate
     * @return the translated content
     */
    public static String translate(String lang, String content) {
        return MAP.computeIfAbsent(lang, s -> new HashMap<>()).computeIfAbsent(content, s -> {
            String building = "";
            String[] contents = content.split("\n");
            int before = 0, after = 0;
            for (int i = 0; i < contents.length; i++) {
                if (contents[i].startsWith(" ")){
                    before = contents[i].indexOf(" ");
                    if (before == -1) {
                        before = 0;
                    }
                }
                for (int j = contents[i].length() - 1; j > -1; --j) {
                    if (contents[i].charAt(j) != ' '){
                        break;
                    }
                    ++after;
                }
                building += FormatHelper.repeat(' ', before) + call(lang, contents[i].substring(before, contents[i].length() - after)) + FormatHelper.repeat(' ', after);
                if (i != contents.length - 1){
                    building += "\n";
                }
            }
            return building;
        });
    }

    /**
     * Calls the Google api to get a translation
     *
     * @param lang the language code to translate to
     * @param content the content to translate
     * @return the translated content
     */
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
    public static String getLangCode(String s){
        return s;//todo
    }
    public static String getLangName(String code){
        return code;
    }
}
