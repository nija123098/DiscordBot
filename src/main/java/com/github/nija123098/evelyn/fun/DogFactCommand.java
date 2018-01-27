package com.github.nija123098.evelyn.fun;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.perms.BotRole;
import com.github.nija123098.evelyn.util.EmoticonHelper;
import com.github.nija123098.evelyn.util.Log;
import com.google.gson.JsonParser;
import com.mashape.unirest.http.Unirest;
import org.apache.commons.text.StringEscapeUtils;

import java.util.Scanner;
import java.util.concurrent.ExecutionException;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class DogFactCommand extends AbstractCommand {
    private static final String DOG = EmoticonHelper.getChars("dog", false) + " ";

    public DogFactCommand() {
        super("dogfact", BotRole.BOT_ADMIN, ModuleLevel.FUN, null, null, "Posts a random dog fact");
    }

    @Command
    public void command(MessageMaker maker) {
        String fact = getDogFact();
        if (fact != null) maker.appendRaw(DOG + StringEscapeUtils.unescapeHtml4(fact));
        else maker.append("Unable to get dog fact, try again later");
    }

    private static String getDogFact() {
        try {
            return new JsonParser().parse(new Scanner(Unirest.get("https://dog-api.kinduff.com/api/facts").asStringAsync().get().getRawBody()).nextLine()).getAsJsonObject().get("facts").getAsString();
        } catch (InterruptedException | ExecutionException e) {
            Log.log("Exception getting dog fact", e);
        }
        return null;
        //return CatFactCommand.getFact();
    }
}
