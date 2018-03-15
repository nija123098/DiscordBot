package com.github.nija123098.evelyn.fun;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.util.InitBuffer;
import com.github.nija123098.evelyn.util.Log;
import com.github.nija123098.evelyn.util.StringHelper;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class InspiroCommand extends AbstractCommand {
    private final InitBuffer<String> buffer = new InitBuffer<>(2, InspiroCommand::getImage);
    public InspiroCommand() {
        super("inspiro", ModuleLevel.FUN, "inspirobot, inspire, inspireme", null, "Gets an image from inspirobot.me");
    }

    @Command
    public void command(MessageMaker maker) {
        maker.withImage(this.buffer.get());
    }

    private static String getImage() {
        try {
            return StringHelper.readAll("http://inspirobot.me/api?generate=true");
        } catch (UnirestException e) {
            Log.log("Exception getting inspiro image", e);
            return null;
        }
    }
}
