package com.github.kaaz.emily.util;

import com.github.kaaz.emily.launcher.BotConfig;
import com.google.api.client.repackaged.com.google.common.base.Joiner;
import org.apache.commons.validator.UrlValidator;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Made by nija123098 on 6/7/2017.
 */
public class YTDLHelper {
    private static final UrlValidator VALIDATOR = new UrlValidator(new String[]{"http", "https"});
    public static boolean download(String url, String id){
        if (!VALIDATOR.isValid(url)) return false;
        String location = BotConfig.AUDIO_PATH + id;
        List<String> commands = new ArrayList<>();
        ProcessBuilder builder = new ProcessBuilder();
        commands.add(BotConfig.YT_DL_PATH);
        //*
        commands.add("--no-check-certificate");
        commands.add("--extract-audio");//-x
        commands.add("--audio-format");
        commands.add(BotConfig.AUDIO_FORMAT);
        //*
        commands.add("--audio-quality");
        commands.add("0");
        commands.add("--prefer-ffmpeg");
        commands.add("--max-filesize");
        commands.add("128m");
        commands.add("--exec");
        commands.add("mv {} " + BotConfig.AUDIO_PATH);//-hide_banner -i input.m4a -c:a copy
        //*/
        commands.add("--output");
        commands.add(location + ".%(ext)s");
        //*/
        commands.add(url);
        System.out.println(Joiner.on(' ').join(commands));
        builder.command(commands);
        builder.redirectErrorStream(true);
        try {
            File file = new File(location + "." + BotConfig.AUDIO_FORMAT);
            file.createNewFile();
            file.getParentFile().mkdirs();
            Process process = builder.start();
            new StreamGobbler(process.getErrorStream(), true).start();
            new StreamGobbler(process.getInputStream(), false).start();
            if (!process.waitFor(2, TimeUnit.MINUTES)){
                if (file.exists()) file.delete();
                return false;
            }
            process.destroy();
        } catch (IOException | InterruptedException e) {
            Log.log("Error while downloading", e);
            return false;
        } finally {
            File malformed = new File(location + ".%(ext)s");
            if (malformed.exists()) malformed.delete();
            return true;
        }
    }
    private static class StreamGobbler extends Thread {
        private InputStream is;
        private boolean err;

        public StreamGobbler(InputStream is, boolean err) {
            this.is = is;
            this.err = err;
        }

        public void run() {
            try {
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String line;
                while ((line = br.readLine()) != null) {
                    (this.err ? System.err : System.out).println(line);
                }
            } catch (IOException ex) {
                // ex.printStackTrace();
            }
        }
    }

}
