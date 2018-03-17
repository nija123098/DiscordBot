package com.github.nija123098.evelyn.util;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.github.nija123098.evelyn.botconfiguration.ConfigProvider.EXECUTABLE_FILES;
import static com.github.nija123098.evelyn.botconfiguration.ConfigProvider.FOLDER_SETTINGS;
import static com.github.nija123098.evelyn.util.Log.log;
import static com.github.nija123098.evelyn.util.NetworkHelper.isValid;
import static java.util.concurrent.TimeUnit.MINUTES;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class YTDLHelper {
    public static boolean download(String url, String id, String format) {
        if (!isValid(url)) return false;
        String location = FOLDER_SETTINGS.audioFolder() + id;
        List<String> commands = new ArrayList<>();
        ProcessBuilder builder = new ProcessBuilder();
        commands.add(EXECUTABLE_FILES.youtubeDl());
        commands.add("--no-check-certificate");
        commands.add("--extract-audio");//-x
        commands.add("--audio-format");
        commands.add(format);
        commands.add("--audio-quality");
        commands.add("0");
        commands.add("--prefer-ffmpeg");
        commands.add("--max-filesize");
        commands.add("1G");
        commands.add("--exec");
        commands.add("mv {} " + FOLDER_SETTINGS.audioFolder());//-hide_banner -i input.m4a -c:a copy
        commands.add("--output");
        commands.add(location + ".%(ext)s");
        commands.add(url);
        builder.command(commands);
        builder.redirectErrorStream(true);
        boolean ret = true;
        try {
            File file = new File(location + "." + format);
            file.getParentFile().mkdirs();
            Process process = builder.start();
            IOUtils.copy(process.getInputStream(), System.out);
            IOUtils.copy(process.getErrorStream(), System.err);
            if (!process.waitFor(2, MINUTES)) {
                if (file.exists()) file.delete();
                ret = false;
            }
            process.destroy();
        } catch (IOException | InterruptedException e) {
            log("Error while downloading", e);
            ret = false;
        } finally {
            File malformed = new File(location + ".%(ext)s");
            if (malformed.exists()) malformed.delete();
        }
        return ret;
    }
}
