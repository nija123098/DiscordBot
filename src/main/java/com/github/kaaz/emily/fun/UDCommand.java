package com.github.kaaz.emily.fun;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.util.EmoticonHelper;
import org.apache.commons.lang3.StringEscapeUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Made by nija123098 on 6/5/2017.
 */
public class UDCommand extends AbstractCommand {
    public UDCommand() {
        super("ud", ModuleLevel.FUN, "urbandictionary", null, "Searches Urban Dictionary");
    }
    @Command
    public void command(MessageMaker maker, String arg){
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new URL("http://api.urbandictionary.com/v0/define?term=" + URLEncoder.encode(arg, "UTF-8")).openConnection().getInputStream()));
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
            reader.close();
            JSONArray listObject = (JSONArray) jsonObject.get("list");
            if (listObject.isEmpty()) {
                maker.getTitle().append("There is no definition for that term!");
                maker.append("[You could go define it!](http://www.urbandictionary.com/add.php?word=" + URLEncoder.encode(arg, "UTF-8") + ")");
                return;
            }
            JSONObject firstResult = (JSONObject) listObject.get(0);
            maker.getTitle().append(firstResult.get("word").toString());
            maker.append(StringEscapeUtils.unescapeHtml4(firstResult.get("definition").toString()));
            if (!firstResult.get("example").toString().isEmpty()) maker.append("\n\n*" + firstResult.get("example") + "*\n");
            maker.append(EmoticonHelper.getChars("+1", false) + firstResult.get("thumbs_up") + "  " + EmoticonHelper.getChars("-1", false) + firstResult.get("thumbs_down"));
            maker.getNote().appendRaw("by " + firstResult.get("author").toString());
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
