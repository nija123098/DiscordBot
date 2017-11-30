package com.github.nija123098.evelyn.util;

import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Made by nija123098 on 6/7/2017.
 */
public class YTDLHelper {
    public static boolean download(String url, String id, String format){
        if (!NetworkHelper.isValid(url)) return false;
        String location = ConfigProvider.FOLDER_SETTINGS.audio_folder() + id;
        List<String> commands = new ArrayList<>();
        ProcessBuilder builder = new ProcessBuilder();
        commands.add(ConfigProvider.LIBRARIES_FILES.youtube_dl());
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
        commands.add("mv {} " + ConfigProvider.FOLDER_SETTINGS.audio_folder());//-hide_banner -i input.m4a -c:a copy
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
            new StreamGobler(process.getInputStream(), System.out).start();
            new StreamGobler(process.getErrorStream(), System.err).start();
            if (!process.waitFor(2, TimeUnit.MINUTES)){
                if (file.exists()) file.delete();
                ret = false;
            }
            process.destroy();
        } catch (IOException | InterruptedException e) {
            Log.log("Error while downloading", e);
            ret = false;
        } finally {
            File malformed = new File(location + ".%(ext)s");
            if (malformed.exists()) malformed.delete();
        }
        return ret;
    }
}
