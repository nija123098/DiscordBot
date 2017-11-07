package com.github.nija123098.evelyn.information;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.exeption.DevelopmentException;
import com.mashape.unirest.http.Unirest;
import org.apache.commons.lang3.StringEscapeUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Made by nija123098 on 7/18/2017.
 */
public class SearchCommand extends AbstractCommand {
    public SearchCommand() {
        super("search", ModuleLevel.INFO, "s, duck, duckduckgo", null, "Searches something using duckduckgo");
    }
    @Command
    public void command(@Argument String search, MessageMaker maker){
        try {
            JSONObject result = (JSONObject) new JSONParser().parse(Unirest.get("https://api.duckduckgo.com/?q=" + StringEscapeUtils.escapeHtml4(search.toLowerCase().replace(" ", "+")) + "&format=json&pretty=1").asString().getBody());
            JSONArray array = ((JSONArray) (result).get("RelatedTopics"));
            if (array.isEmpty()){
                maker.append("We didn't get any results for that.  Make sure it is spelled correctly!");
                return;
            }
            JSONObject topic = ((JSONObject) array.get(0));
            maker.getTitle().appendRaw(result.get("Heading").toString());
            maker.append(topic.get("Text").toString());
            maker.withUrl(topic.get("FirstURL").toString());
            String imageURL = ((JSONObject) topic.get("Icon")).get("URL").toString();
            maker.withImage(imageURL);
            maker.withColor(imageURL);
        } catch (Exception e) {
            throw new DevelopmentException("Sorry, duckduckgo seems to be having issue right now", e);
        }
    }
}
