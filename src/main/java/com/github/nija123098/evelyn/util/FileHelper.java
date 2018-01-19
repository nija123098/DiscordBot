package com.github.nija123098.evelyn.util;

import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import com.github.nija123098.evelyn.exception.DevelopmentException;
import org.eclipse.jetty.util.ConcurrentHashSet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class FileHelper {
    private static final Set<File> FILES = new ConcurrentHashSet<>();
    private static final AtomicInteger UNIQUE_INTEGER = new AtomicInteger();
    public static File getTempFile(String cat, String end){
        return getTempFile(cat, end, "gen" + UNIQUE_INTEGER.incrementAndGet(), file -> {});
    }
    public static File getTempFile(String cat, String end, String snowflake){
        return getTempFile(cat,  end, snowflake, file -> {});
    }
    public static File getTempFile(String cat, String end, String snowflake, IOConsumer once){
        File file;
        try{file = Paths.get(ConfigProvider.FOLDER_SETTINGS.tempFolder(), cat, snowflake + "." + end).toFile();
        }catch(Exception e){throw new DevelopmentException("Issue with making new file", e);}
        file.deleteOnExit();
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {file.createNewFile();}
            catch(IOException e){throw new DevelopmentException("Could not make file: " + file.getPath(), e);}
            try{once.accept(file);
            } catch (IOException e) {
                throw new DevelopmentException("Exception writing to file", e);
            }
        }
        return file;
    }
    public static void clearTemps(){
        Set<File> files = new HashSet<>(FILES);
        FILES.removeAll(files);
        files.forEach(File::delete);
        FILES.clear();
    }
    public interface IOConsumer {
        void accept(File file) throws IOException;
    }
}