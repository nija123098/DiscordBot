package com.github.kaaz.emily.fun;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.DiscordClient;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.util.EmoticonHelper;
import com.google.gson.JsonParser;
import org.apache.commons.lang3.StringEscapeUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Made by nija123098 on 5/21/2017.
 */
public class CatFactCommand extends AbstractCommand {
    public CatFactCommand() {
        super("catfact", ModuleLevel.FUN, null, null, "Cat Facts!");
    }
    @Command
    public void command(MessageMaker maker, Guild guild){
        String fact = getCatFact();
        if (fact != null) maker.appendRaw(EmoticonHelper.getChars("cat") + "  " + StringEscapeUtils.escapeHtml4(fact));
        else maker.append("Unable to get cat fact, try again later");
    }
    private static String getCatFact() {
        return getFact("http://catfacts-api.appspot.com/api/facts");
    }
    public static String getFact(String url){
        try {
            URL loginurl = new URL(url);
            URLConnection yc = loginurl.openConnection();
            yc.setConnectTimeout(10_000);
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            String inputLine = in.readLine();
            return new JsonParser().parse(inputLine).getAsJsonObject().get("facts").getAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
