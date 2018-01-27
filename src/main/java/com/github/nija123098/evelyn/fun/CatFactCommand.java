package com.github.nija123098.evelyn.fun;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.util.EmoticonHelper;
import com.github.nija123098.evelyn.util.Log;
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
public class CatFactCommand extends AbstractCommand {
    public CatFactCommand() {
        super("catfact", ModuleLevel.FUN, null, null, "Cat Facts!");
    }

    @Command
    public void command(MessageMaker maker) {
        String fact = getCatFact();
        if (fact != null)
            maker.appendRaw(EmoticonHelper.getChars("cat", false) + "  " + StringEscapeUtils.unescapeHtml4(fact));
        else maker.append("Unable to get cat fact, try again later");
    }

    private static String getCatFact() {
        return getFact("https://catfact.ninja/fact");
    }

    public static String getFact(String url) {
        try {
            URLConnection yc = new URL(url).openConnection();
            yc.setConnectTimeout(10_000);
            return new JsonParser().parse(new BufferedReader(new InputStreamReader(yc.getInputStream())).readLine()).getAsJsonObject().get("fact").getAsString();
        } catch (Exception e) {
            Log.log("Exception getting cat facts", e);
        }
        return null;
    }
}
