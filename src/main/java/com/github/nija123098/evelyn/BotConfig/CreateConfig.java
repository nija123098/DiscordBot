/**
 * @author Celestialdeath99
 */

package com.github.nija123098.evelyn.BotConfig;

import com.github.nija123098.evelyn.util.PlatformDetector;
import org.apache.commons.io.FileUtils;
import java.io.*;

import static com.github.nija123098.evelyn.util.PlatformDetector.ConverPath;
import static com.github.nija123098.evelyn.util.PlatformDetector.PathEnding;

public class CreateConfig {

    public static void main() throws IOException {

        String FILEPATH = ConverPath(System.getProperty("user.dir"));
        String FOLDERPATH = FILEPATH + PathEnding();
        String youtube_dl = null;

        String ConfigTemplate = "####################\n"
                + "### Bot Settings ###\n"
                + "####################\n"
                + "Bot-Token=\n"
                + "Test-Mode-Enabled=false\n"
                + "Total-Evelyn-Shards=1\n"
                + "Evelyn-Shard-Number=0\n"
                + "Support-Server-ID=\n"
                + "Ghost-Mode-Enabled=false\n"
                + "Message-Filtering-Server-Size=150\n"
                + "Typing-Enabled=false\n"
                + "Voice-Commands-Enabled=false\n"
                + "Contributor-Sign-Role=-1\n"
                + "Supporter-Sign-Role=-1\n\n"
                + "########################\n"
                + "### Database Settigs ###\n"
                + "########################\n"
                + "Database-IP=localhost\n"
                + "Database-Port=3306\n"
                + "Database-Username=username\n"
                + "Database-Password=password\n"
                + "Database-Name=DiscordBotDB\n\n"
                + "#########################\n"
                + "### Service Auth Keys ###\n"
                + "#########################\n"
                + "Bitly-Token=\n"
                + "Cat-API-Token=\n"
                + "Giphy-API-Token=\n"
                + "Google-API-Key=\n"
                + "Riot-Games-Token=\n"
                + "Twitch-ID=\n"
                + "Twitter-Key=\n"
                + "Twitter-Secret-Key=\n\n"
                + "######################\n"
                + "### Audio Settings ###\n"
                + "######################\n"
                + "Audio-File-Types=opus, mp3\n"
                + "Audio-Format=mp3\n"
                + "Music-Downloader-Threads=1\n"
                + "Track-Expiration-Time=432000000\n"
                + "Required-Plays-To-Download=100\n\n"
                + "####################\n"
                + "### Folder Paths ###\n"
                + "####################\n"
                + "Audio-Path=" + FOLDERPATH + "AudioFiles" + PathEnding() + "\n"
                + "Badges-Path=" + FOLDERPATH + "Badges" + PathEnding() + "\n"
                + "Libraries-Folder=" + FOLDERPATH + "Libraries" + PathEnding() + "\n"
                + "Logs-Path=" + FOLDERPATH + "Logs" + PathEnding() + "\n"
                + "Neural-Net-Path=" + FOLDERPATH + "NeuralNet" + PathEnding() + "\n"
                + "Temp-Path=" + FOLDERPATH + "Temp" + PathEnding() + "\n"
                + "Required-Files-Path=" + FOLDERPATH + "Required" + PathEnding() + "\n\n"
                + "###########################\n"
                + "### Required File Names ###\n"
                + "###########################\n"
                + "Emoticons=Emoticons.txt\n"
                + "Fake-Danger=FakeDanger.txt\n"
                + "Final-Map=FinalMap.png\n"
                + "Guide=Guide.txt\n"
                + "Language-Filtering=LanguageFiltering.txt\n"
                + "Supported-Languages=SupportedLangs.txt\n"
                + "Time-Stats=TimeStats.txt\n"
                + "Verified-Games-List=VerifiedGames.txt\n\n"
                + "#######################\n"
                + "### Libraries Paths ###\n"
                + "#######################\n"
                + "ffm_peg=" + FOLDERPATH + "Libraries" + PathEnding() + "ffmpeg.exe\n"
                + "youtube-dl=";

        if (PlatformDetector.isWindows()) {
            youtube_dl = FOLDERPATH + "Libraries" + PathEnding() + "youtube-dl.exe";
        } else  if (PlatformDetector.isUnix()) {
            youtube_dl = "/usr/local/bin/youtube-dl";
        }

        FileUtils.writeStringToFile(new File(FILEPATH + PathEnding() + "Bot-Config.cfg"), ConfigTemplate + youtube_dl);
    }
}
