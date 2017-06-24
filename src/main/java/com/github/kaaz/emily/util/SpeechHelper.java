package com.github.kaaz.emily.util;

import com.darkprograms.speech.synthesiser.Synthesiser;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Made by nija123098 on 3/28/2017.
 */
public class SpeechHelper {
    private static final Map<String, Map<String, File>> MAP = new ConcurrentHashMap<>();
    private static final Synthesiser SYNTHESISER = new Synthesiser();
    public static File getFile(LangString string, String lang){
        String content = string.translate(lang);
        return MAP.computeIfAbsent(lang, s -> new HashMap<>()).computeIfAbsent(content, s -> {
            try {
                File file = FileHelper.getTempFile("speech", "mp3");
                SYNTHESISER.setLanguage(lang);
                IOUtils.copy(SYNTHESISER.getMP3Data(content), new FileOutputStream(file));
                MAP.get(lang).put(content, file);
                return file;
            } catch (IOException e) {
                throw new RuntimeException("Error during audio synthesis", e);
            }
        });
    }
}
