package com.github.nija123098.evelyn.information;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.exception.DevelopmentException;
import com.github.nija123098.evelyn.util.Log;
import com.mashape.unirest.http.Unirest;
import org.apache.commons.text.StringEscapeUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class SearchCommand extends AbstractCommand {
    public SearchCommand() {
        super("search", ModuleLevel.INFO, "s, duck, duckduckgo", null, "Searches something using duckduckgo");
    }
    @Command
    public void command(@Argument String search, MessageMaker maker) {
        if (search.isEmpty()) maker.append("Please add what you would like to search as an argument.  For example @Evelyn search AI overlord");
        else try {
            JSONObject result = (JSONObject) new JSONParser().parse(Unirest.get("https://api.duckduckgo.com/?q=" + StringEscapeUtils.escapeHtml4(search.toLowerCase().replace(" ", "+")) + "&format=json&pretty=1").asString().getBody());
            JSONArray array = ((JSONArray) (result).get("RelatedTopics"));
            if (array.isEmpty()) {
                maker.append("We didn't get any results for that.  Make sure it is spelled correctly!");
                return;
            }
            JSONObject topic = (JSONObject) array.get(0);
            maker.getTitle().appendRaw(result.get("Heading").toString());
            JSONObject imageURLObject = (JSONObject) topic.get("Icon");
            if (imageURLObject != null) {
                String imageURL = imageURLObject.get("URL").toString();
                if (imageURL != null) {
                    maker.withThumb(imageURL);
                    maker.withColor(imageURL);
                }
            }
            maker.appendRaw(topic.get("Text").toString().replace("...", "."));
            maker.withUrl(topic.get("FirstURL").toString());
        } catch (Exception e) {
            Log.log("Exception while searching " + search);
            throw new DevelopmentException("Sorry, duckduckgo seems to be having issue right now", e);
        }
    }
}
