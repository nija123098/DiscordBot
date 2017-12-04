package com.github.nija123098.evelyn.fun;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;

import static com.github.nija123098.evelyn.botconfiguration.ConfigProvider.AUTH_KEYS;
import static com.github.nija123098.evelyn.command.ModuleLevel.FUN;
import static com.google.common.base.Joiner.on;
import static com.mashape.unirest.http.Unirest.get;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class GifCommand extends AbstractCommand {
    public GifCommand() {
        super("gif", FUN, null, null, "Gets a gif from giphy");
    }

    @Command
    public void command(MessageMaker maker, String[] args) {
        try {
            String tags = "";
            if (args.length > 0) {
                tags = "&tag=" + on("+").join(args);
            }
            HttpResponse<JsonNode> response = get("http://api.giphy.com/v1/gifs/random?api_key=" + AUTH_KEYS.giphy_token() + tags).asJson();
            maker.appendRaw(response.getBody().getObject().getJSONObject("data").getString("url"));
        } catch (Exception ignored) {
        }
        maker.append("Sorry, there are no gifs right now, check back later.");
    }

    @Override
    protected String getLocalUsages() {
        return "#  gif <tag> // Get a gif from giphy based on <tag>";
    }
}
