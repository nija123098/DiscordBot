package com.github.nija123098.evelyn.fun;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.util.Log;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.text.StringEscapeUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class JokeCommand extends AbstractCommand {
    private static final String[] REDDIT_OPTIONS = new String[0];

    public JokeCommand() {
        super("joke", ModuleLevel.FUN, null, null, "Let me tell you a joke!");
    }

    @Command
    public void command(MessageMaker maker, User user) {// todo add Reddit jokes
        String joke = getJokeFromWeb(user.getName());
        try {
            RedditCommand.command(REDDIT_OPTIONS, maker);
        } catch (Exception e) {
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
