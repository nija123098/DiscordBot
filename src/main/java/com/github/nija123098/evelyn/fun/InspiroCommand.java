package com.github.nija123098.evelyn.fun;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;

import java.io.IOException;

import static com.github.nija123098.evelyn.command.ModuleLevel.FUN;
import static com.github.nija123098.evelyn.util.Log.log;
import static org.jsoup.Jsoup.connect;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class InspiroCommand extends AbstractCommand {
    public InspiroCommand() {
        super("inspiro", FUN, "inspirobot, inspire, inspireme", null, "Gets an image from inspirobot.me");
    }

    @Command
    public void command(MessageMaker maker) {
        maker.withImage(getImage());
    }

    private static String getImage() {
        try {
            return connect("http://inspirobot.me/api?generate=true").timeout(5_000).userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36").get().body().getElementsByTag("body").text();
        } catch (IOException e) {
            log("Exception getting inspiro image", e);
            return null;
        }
    }
}
