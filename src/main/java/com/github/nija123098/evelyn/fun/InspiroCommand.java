package com.github.nija123098.evelyn.fun;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.launcher.BotConfig;
import com.github.nija123098.evelyn.util.Log;
import org.jsoup.Jsoup;

import java.io.IOException;

/**
 * Made by nija123098 on 6/28/2017.
 */
public class InspiroCommand extends AbstractCommand {
    public InspiroCommand() {
        super("inspiro", ModuleLevel.FUN, "inspirobot, inspire, inspireme", null, "Gets an image from inspirobot.me");
    }
    @Command
    public void command(MessageMaker maker){
        maker.withImage(getImage());
    }
    private static String getImage(){
        try {
            return Jsoup.connect("http://inspirobot.me/api?generate=true").timeout(5_000).userAgent(BotConfig.USER_AGENT).get().body().getElementsByTag("body").text();
        } catch (IOException e) {
            Log.log("Exception getting inspiro image", e);
            return null;
        }
    }
}
