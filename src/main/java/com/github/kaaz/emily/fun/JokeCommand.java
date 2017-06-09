package com.github.kaaz.emily.fun;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.util.RedditLink;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Made by nija123098 on 5/22/2017.
 */
public class JokeCommand extends AbstractCommand {
    public JokeCommand() {
        super("joke", ModuleLevel.FUN, null, null, "");
    }
    @Command
    public void handle(MessageMaker maker, User user){// todo add Reddit jokes
        String joke = getJokeFromWeb(user.getName());
        maker.append(joke != null && !joke.isEmpty() ? joke : "No jokes available, try latter");
    }
    private String getJokeFromWeb(String username) {
        try {
            URL loginurl = new URL("http://api.icndb.com/jokes/random?firstName=&lastName=" + username);
            URLConnection yc = loginurl.openConnection();
            yc.setConnectTimeout(10_000);
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            String inputLine = in.readLine();
            JsonParser parser = new JsonParser();
            JsonObject array = parser.parse(inputLine).getAsJsonObject();
            return array.get("value").getAsJsonObject().get("joke").getAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
