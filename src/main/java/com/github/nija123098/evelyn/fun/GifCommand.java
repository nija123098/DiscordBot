package com.github.nija123098.evelyn.fun;

import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.google.common.base.Joiner;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

/**
 * Made by nija123098 on 5/25/2017.
 */
public class GifCommand extends AbstractCommand {
    public GifCommand() {
        super("gif", ModuleLevel.FUN, null, null, "Gets a gif from giphy");
    }
    @Command
    public void command(MessageMaker maker, String[] args){
        try {
            String tags = "";
            if (args.length > 0) {
                tags = "&tag=" + Joiner.on("+").join(args);
            }
            HttpResponse<JsonNode> response = Unirest.get("http://api.giphy.com/v1/gifs/random?api_key=" + ConfigProvider.authKeys.giphy_token() + tags).asJson();
            maker.appendRaw(response.getBody().getObject().getJSONObject("data").getString("url"));
        } catch (Exception ignored) {}
        maker.append("Sorry, there are no gifs right now, check back later.");
    }

    @Override
    protected String getLocalUsages() {
        return "#  gif <tag> // Get a gif from giphy based on <tag>";
    }
}
