package com.github.nija123098.evelyn.util;

import com.github.nija123098.evelyn.exeption.ArgumentException;
import com.github.nija123098.evelyn.exeption.DevelopmentException;
import com.github.nija123098.evelyn.launcher.BotConfig;
import com.github.nija123098.evelyn.launcher.Reference;
import javafx.util.Pair;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Assists in translation for segments of {@link String}
 * where part of the {@link String} is not to be translated.
 *
 * @author nija123098
 * @since 1.0.0
 */
public class LangString {
    /**
     * The value of the lang string in pairs
     * of strings and booleans, representing
     * if that string should be translated.
     */
    private final List<Pair<Boolean, String>> value = new ArrayList<>();
    private final Map<String, String> translated = new HashMap<>(1);
    public LangString() {}
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
    public LangString append(LangString langString){
        this.value.addAll(langString.value);
        return this;
    }
    public boolean hasContents(){
        return this.value.size() > 0;
    }
    public String translate(String lang) {
        return this.translated.computeIfAbsent(lang, s -> {
            final StringBuilder builder = new StringBuilder();
            this.value.forEach(pair -> builder.append(pair.getKey() ? translate(lang, pair.getValue()) : pair.getValue()));
            return builder.toString().replace("\n", Reference.NL);
        });
    }

    /**
     * Returns the value of the string
     * without translation, which should be
     * equivalent to translating the LangString
     * with the original language.
     *
     * @return the value without any translation.
     */
    public String asBuilt() {
        final StringBuilder builder = new StringBuilder();
        value.forEach(pair -> builder.append(pair.getValue()));
        return builder.toString();
    }

    /**
     * The cache for API calls for translated content.
     */
    private static final Map<String, Map<String, String>> MAP = new HashMap<>();

    /**
     * Translates the content, but unlike
     * {@link LangString#call(String, String)}
     * it formats the calls properly.
     *
     * This is used to ensure that the requested translation
     * is short enough or it is split into fragments for translation.
     *
     * @param lang the lang code to translate to.
     * @param content the content to translate.
     * @return the translated content.
     */
    public static String translate(String lang, String content){
        if (content.length() < 101) return preCall(lang, content);
        StringBuilder total = new StringBuilder();
        String building = "";
        String[] split = content.split(Pattern.quote(". "));
        for (int i = 0; i < split.length; i++) {
            if (split[i].length() + building.length() > 100){
                --i;
                total.append(preCall(building, lang));
                building = "";
            }
            if (i == split.length - 1) return total.append(preCall(building, lang)).toString();
        }
        throw new DevelopmentException("nija123098 did something horribly wrong!");
    }// should never be called since "i == split.length - 1" should happen on the last iteration

    /**
     * Translates the content, but unlike
     * {@link LangString#call(String, String)}
     * it formats the calls properly.
     *
     * @param lang the lang code to translate to.
     * @param content the content to translate.
     * @return the translated content.
     */
    private static String preCall(String lang, String content) {
        return MAP.computeIfAbsent(lang, s -> new HashMap<>()).computeIfAbsent(content, s -> {
            if (content == null) return null;
            if (content.equals("\n")) return "\n";
            String building = "";
            boolean nextLineLast = content.endsWith("\n");
            String[] contents = content.split("\n");
            int before = 0, after = 0;
            for (int i = 0; i < contents.length; i++) {
                for (int j = 0; j < contents[i].length(); j++) {
                    if (contents[i].charAt(j) == ' ') ++before;
                    else break;
                }
                for (int j = contents[i].length() - 1; j > -1; --j) {
                    if (contents[i].charAt(j) != ' '){
                        break;
                    }
                    ++after;
                }
                building += FormatHelper.repeat(' ', before) + call(lang, contents[i].substring(before, contents[i].length() - after)) + FormatHelper.repeat(' ', after);
                if (i != contents.length - 1 || nextLineLast){
                    building += "\n";
                }
                before = 0;
                after = 0;
            }
            return building;
        });
    }

    /**
     * Calls the Google api to get a translation
     *
     * @param lang the language code to translate to.
     * @param content the content to translate.
     * @return the translated content.
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
                    throw new DevelopmentException("Exception while attempting to get translation", e);
                }
            });
        } catch (DevelopmentException e) {
            return content;
        }
    }
    private static final Map<String, String> LANG_MAP = new HashMap<>();
    private static Map<String, String> REVERSE_MAP;
    static {
        try{Files.readAllLines(Paths.get(BotConfig.CONTAINER_PATH, "SupportedLangs.txt")).forEach(s -> {
            String[] strings = s.toLowerCase().split(" ");
            LANG_MAP.put(strings[0], strings[1]);
        });
        }catch(IOException e){throw new DevelopmentException("Could not load supported languages", e);}
        REVERSE_MAP = new HashMap<>(LANG_MAP.size());
        LANG_MAP.forEach((s, s2) -> REVERSE_MAP.putIfAbsent(s2, s));
    }
    public static boolean isLangCode(String code){
        return REVERSE_MAP.containsKey(code.toLowerCase());
    }
    public static String getLangCode(String s){
        String code = LANG_MAP.get(s.toLowerCase());
        if (code == null) throw new ArgumentException("Invalid language: " + s);
        return code;
    }
    public static boolean isLangName(String code){
        return LANG_MAP.containsKey(code.toLowerCase());
    }
    public static String getLangName(String code){
        if (code == null) return "not set";
        String name = REVERSE_MAP.get(code.toLowerCase());
        if (name == null) throw new ArgumentException("Invalid language code, use ISO: " + code);
        return name;
    }
}
