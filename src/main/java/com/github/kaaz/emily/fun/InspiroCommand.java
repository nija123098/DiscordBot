package com.github.kaaz.emily.fun;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.launcher.BotConfig;
import org.jsoup.Jsoup;

import java.io.IOException;

/**
 * Made by nija123098 on 6/28/2017.
 */
public class InspiroCommand extends AbstractCommand {
    public InspiroCommand() {
        super("inspiro", ModuleLevel.FUN, "inspirobot, inspire, inspireme", null, "Gets a image from inspirobot.me");
    }
    @Command
    public void command(MessageMaker maker){
        maker.withImage(getImage());
    }
    private static String getImage(){
        try {
            return Jsoup.connect("http://inspirobot.me/api?generate=true").timeout(5_000).userAgent(BotConfig.USER_AGENT).get().body().getElementsByTag("body").text();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
