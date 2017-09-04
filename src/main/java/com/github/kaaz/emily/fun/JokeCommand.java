package com.github.kaaz.emily.fun;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.util.Log;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.lang3.StringEscapeUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Made by nija123098 on 5/22/2017.
 */
public class JokeCommand extends AbstractCommand {
    private static final String[] REDDIT_OPTIONS = new String[0];
    public JokeCommand() {
        super("joke", ModuleLevel.FUN, null, null, "Let me tell you a joke!");
    }
    @Command
    public void command(MessageMaker maker, User user){// todo add Reddit jokes
        String joke = getJokeFromWeb(user.getName());
        try{RedditCommand.command(REDDIT_OPTIONS, maker);
        } catch (Exception e){
            maker.append(joke != null && !joke.isEmpty() ? joke : "No jokes available, try later");
        }
    }
    private String getJokeFromWeb(String username) {
        try {
            URL loginurl = new URL("http://api.icndb.com/jokes/random?firstName=&lastName=UNIQUE_SEQUENCE");
            URLConnection yc = loginurl.openConnection();
            yc.setConnectTimeout(10_000);
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            String inputLine = in.readLine();
            JsonParser parser = new JsonParser();
            JsonObject array = parser.parse(inputLine).getAsJsonObject();
            return StringEscapeUtils.unescapeHtml4(array.get("value").getAsJsonObject().get("joke").getAsString()).replace("UNIQUE_SEQUENCE", username);
        } catch (Exception e) {
            Log.log("Exception loading joke from web", e);
        }
        return null;
    }

}
