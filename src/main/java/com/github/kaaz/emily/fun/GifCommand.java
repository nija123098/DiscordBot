package com.github.kaaz.emily.fun;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.launcher.BotConfig;
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
            HttpResponse<JsonNode> response = Unirest.get("http://api.giphy.com/v1/gifs/random?api_key=" + BotConfig.GIPHY_TOKEN + tags).asJson();
            maker.appendRaw(response.getBody().getObject().getJSONObject("data").getString("url"));
        } catch (Exception ignored) {}
        maker.append("Sorry, there are no gifs right now, check back later.");
    }
}
