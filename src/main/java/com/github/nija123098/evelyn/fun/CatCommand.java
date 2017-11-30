package com.github.nija123098.evelyn.fun;

import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.util.EmoticonHelper;
import com.github.nija123098.evelyn.util.Log;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

/**
 * Made by nija123098 on 6/19/2017.
 */
public class CatCommand extends AbstractCommand {
    private static final String CAT = EmoticonHelper.getChars("cat", false) + " ";
    private static final String DIDNT_WORK = "Unfortunately I can't seem to get any cats for you right now. " + EmoticonHelper.getChars("crying_cat_face", false);
    private static final String URL;
    static {
        String builder = "http://thecatapi.com/api/images/get?type=gif&format=html";
        if (ConfigProvider.AUTH_KEYS.cat_api_token() != null) builder += "&api_key=" + ConfigProvider.AUTH_KEYS.cat_api_token();
        URL = builder;
    }
    public CatCommand() {
        super("cat", ModuleLevel.FUN, null, "cat", "Displays a random cat gif");
    }
    @Command
    public void command(MessageMaker maker){
        try {
            String src = new Scanner(new URL(URL).openStream(), "UTF-8").nextLine();
            maker.getTitle().append("CAT!");
            maker.withImage(src.substring(src.indexOf("><img src=") + 11, src.length() - 6));
        } catch (IOException e) {
            Log.log("Exception getting cat image", e);
            maker.append(DIDNT_WORK);
        }
    }
}
