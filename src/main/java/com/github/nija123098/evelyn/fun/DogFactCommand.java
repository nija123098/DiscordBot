package com.github.nija123098.evelyn.fun;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.google.gson.JsonParser;

import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import static com.github.nija123098.evelyn.command.ModuleLevel.FUN;
import static com.github.nija123098.evelyn.perms.BotRole.BOT_ADMIN;
import static com.github.nija123098.evelyn.util.EmoticonHelper.getChars;
import static com.github.nija123098.evelyn.util.Log.log;
import static com.mashape.unirest.http.Unirest.get;
import static org.apache.commons.lang3.StringEscapeUtils.unescapeHtml4;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class DogFactCommand extends AbstractCommand {
    private static final String DOG = getChars("dog", false) + " ";

    public DogFactCommand() {
        super("dogfact", BOT_ADMIN, FUN, null, null, "Posts a random dog fact");
    }

    @Command
    public void command(MessageMaker maker) {
        String fact = getDogFact();
        if (fact != null) maker.appendRaw(DOG + unescapeHtml4(fact));
        else maker.append("Unable to get dog fact, try again later");
    }

    private static String getDogFact() {
        try {
            return new JsonParser().parse(new Scanner(get("https://dog-api.kinduff.com/api/facts").asStringAsync().get().getRawBody()).nextLine()).getAsJsonObject().get("facts").getAsString();
        } catch (InterruptedException | ExecutionException e) {
            log("Exception getting dog fact", e);
        }
        return null;
        //return CatFactCommand.getFact();
    }
}
