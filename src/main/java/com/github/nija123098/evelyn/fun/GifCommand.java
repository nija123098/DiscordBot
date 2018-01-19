package com.github.nija123098.evelyn.fun;

import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.util.Log;
import com.google.common.base.Joiner;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class GifCommand extends AbstractCommand {
    public GifCommand() {
        super("gif", ModuleLevel.FUN, null, null, "Gets a gif from giphy");
    }

    @Command
    public void command(MessageMaker maker, String[] args) {
        String tags = "";
        if (args.length > 0) {
            tags = "&tag=" + Joiner.on("+").join(args);
        }
        try {
            maker.appendRaw(Unirest.get("http://api.giphy.com/v1/gifs/random?api_key=" + ConfigProvider.AUTH_KEYS.giphyToken() + tags).asJson().getBody().getObject().getJSONObject("data").getString("url"));
        } catch (UnirestException e) {
            Log.log("Could not get gifs from giphy.com", e);
        }
        maker.append("Sorry, there are no gifs right now, check back later.");
    }

    @Override
    protected String getLocalUsages() {
        return "#  gif <tag> // Get a gif from giphy based on <tag>";
    }
}
