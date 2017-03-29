package com.github.kaaz.emily.util;

import com.darkprograms.speech.synthesiser.Synthesiser;
import com.github.kaaz.emily.programconfig.BotConfig;
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
    private static final File PARENT = new File(BotConfig.TEMP_PATH);
    public static File getFile(LangString string, String lang){
        try {
            File file = File.createTempFile("audio", "mp3", PARENT);
            file.deleteOnExit();
            SYNTHESISER.setLanguage(lang);
            String content = string.translate(lang);
            IOUtils.copy(SYNTHESISER.getMP3Data(content), new FileOutputStream(file));
            MAP.computeIfAbsent(lang, s -> new HashMap<>()).put(content, file);
            return file;
        } catch (IOException e) {
            throw new RuntimeException("Error during audio synthesis", e);
        }
    }
}
