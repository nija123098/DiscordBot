package com.github.nija123098.evelyn.fun;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import static com.github.nija123098.evelyn.command.ModuleLevel.FUN;
import static com.github.nija123098.evelyn.util.EmoticonHelper.getChars;
import static com.github.nija123098.evelyn.util.Log.log;
import static org.apache.commons.lang3.StringEscapeUtils.unescapeHtml4;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class CatFactCommand extends AbstractCommand {
    public CatFactCommand() {
        super("catfact", FUN, null, null, "Cat Facts!");
    }

    @Command
    public void command(MessageMaker maker, Guild guild) {
        String fact = getCatFact();
        if (fact != null)
            maker.appendRaw(getChars("cat", false) + "  " + unescapeHtml4(fact));
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
            log("Exception getting cat facts", e);
        }
        return null;
    }
}
