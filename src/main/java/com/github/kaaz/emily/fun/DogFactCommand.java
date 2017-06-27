package com.github.kaaz.emily.fun;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.perms.BotRole;
import com.github.kaaz.emily.util.EmoticonHelper;
import com.google.gson.JsonParser;
import com.mashape.unirest.http.Unirest;
import org.apache.commons.lang3.StringEscapeUtils;

import java.util.Scanner;
import java.util.concurrent.ExecutionException;

/**
 * Made by nija123098 on 6/19/2017.
 */
public class DogFactCommand extends AbstractCommand {
    private static final String DOG = EmoticonHelper.getChars("dog") + " ";
    public DogFactCommand() {
        super("dogfact", BotRole.BOT_ADMIN, ModuleLevel.FUN, null, null, "Posts a random dog fact");
    }
    @Command
    public void command(MessageMaker maker){//
        String fact = getDogFact();
        if (fact != null) maker.appendRaw(DOG + StringEscapeUtils.escapeHtml4(fact));
        else maker.append("Unable to get dog fact, try again later");
    }
    private static String getDogFact(){
        try {
            return new JsonParser().parse(new Scanner(Unirest.get("https://dog-api.kinduff.com/api/facts").asStringAsync().get().getRawBody()).nextLine()).getAsJsonObject().get("facts").getAsString();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
        //return CatFactCommand.getFact();
    }
}
