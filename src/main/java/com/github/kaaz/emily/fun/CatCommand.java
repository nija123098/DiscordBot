package com.github.kaaz.emily.fun;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.launcher.BotConfig;
import com.github.kaaz.emily.util.EmoticonHelper;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

/**
 * Made by nija123098 on 6/19/2017.
 */
public class CatCommand extends AbstractCommand{
    private static final String CAT = EmoticonHelper.getChars("cat") + " ";
    private static final String DIDNT_WORK = "Unfortunately I can't seem to get any cats for you right now. " + EmoticonHelper.getChars("crying_cat_face");
    private static final String URL;
    static {
        String builder = "http://thecatapi.com/api/images/get?type=gif&format=html";
        if (BotConfig.CAT_API_TOKEN != null) builder += "&api_key=" + BotConfig.CAT_API_TOKEN;
        URL = builder;
    }
    public CatCommand() {
        super("cat", ModuleLevel.FUN, null, "cat", "Displays a random cat gif");
    }
    @Command
    public void command(MessageMaker maker){
        try {
            String src = new Scanner(new URL(URL).openStream(), "UTF-8").nextLine();
            maker.appendRaw(CAT + src.substring(src.indexOf("><img src=") + 11, src.length() - 6));
        } catch (IOException e) {
            e.printStackTrace();
            maker.append(DIDNT_WORK);
        }
    }
}
