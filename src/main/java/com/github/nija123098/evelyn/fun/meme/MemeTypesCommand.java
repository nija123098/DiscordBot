package com.github.nija123098.evelyn.fun.meme;

import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.util.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class MemeTypesCommand extends AbstractCommand {
    static final Set<String> MEME_TYPES = ConcurrentHashMap.newKeySet();
    public MemeTypesCommand() {
        super(MemeCommand.class, "types", null, null, "list", "List of all valid types");
        loadMemeTypes();
    }
    @Command
    public static void command(MessageMaker maker) {
        maker.mustEmbed();
        if (MEME_TYPES.isEmpty()) loadMemeTypes();
        ArrayList<String> memes = new ArrayList<>(MEME_TYPES);
        Collections.sort(memes);
        int count = 0;
        for (String s : memes) {
            maker.getNewListPart().appendRaw(s);
            count++;
            if (count % 20 == 0) {
                maker.guaranteeNewListPage();
            }
        }
    }
    static void loadMemeTypes() {
        try {
            Document document = Jsoup.connect("https://memegen.link/").userAgent(ConfigProvider.BOT_SETTINGS.userAgent()).timeout(5_000).get();
            if (document != null) {
                Elements fmls = document.select(".js-meme-selector option");
                if (!fmls.isEmpty()) {
                    for (Element fml : fmls) MEME_TYPES.add(fml.val().toLowerCase());
                }
            }
        } catch (IOException e) {
            Log.log("Exception loading meme types", e);
        }
    }
}
