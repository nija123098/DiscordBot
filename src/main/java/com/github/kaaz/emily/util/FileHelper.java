package com.github.kaaz.emily.util;

import com.github.kaaz.emily.exeption.DevelopmentException;
import com.github.kaaz.emily.launcher.BotConfig;
import org.eclipse.jetty.util.ConcurrentHashSet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Made by nija123098 on 6/6/2017.
 */
public class FileHelper {
    private static final Set<File> FILES = new ConcurrentHashSet<>();
    private static final AtomicInteger i = new AtomicInteger();
    public static File getTempFile(String cat, String end){
        return getTempFile(cat, end, "gen" + i.incrementAndGet());
    }
    public static File getTempFile(String cat, String end, String snowflake){
        File file;
        try{file = Paths.get(BotConfig.TEMP_PATH, cat, snowflake + "." + end).toFile();
        }catch(Exception e){throw new DevelopmentException("Issue with making new file", e);}
        //file.deleteOnExit();
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {file.createNewFile();}
            catch(IOException e){throw new DevelopmentException(e);}
        }
        return file;
    }
    public static void clearTemps(){
        Set<File> files = new HashSet<>(FILES);
        FILES.removeAll(files);
        files.forEach(File::delete);
        FILES.clear();
    }
}
