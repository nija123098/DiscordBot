package com.github.nija123098.evelyn.fun.stories;

import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.github.nija123098.evelyn.discordobjects.wrappers.VoiceChannel;
import com.github.nija123098.evelyn.exception.DevelopmentException;
import com.github.nija123098.evelyn.util.FormatHelper;
import com.github.nija123098.evelyn.util.LangString;
import com.github.nija123098.evelyn.util.StringHelper;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class StoriesCommand extends AbstractCommand {
    private static final List<String> STORY_TITLES = new ArrayList<>();
    private static final List<String> REDUCED_LIST = new ArrayList<>();
    private static final Map<String, String> STORY_MAP = new HashMap<>();
    public StoriesCommand() {
        super("stories", ModuleLevel.FUN, "story", null, "Shows a list of bed time stories or reads one to you, from tonightsbedtimestory.com");
        try {
            if (ConfigProvider.BOT_SETTINGS.testModeEnabled()) return;
            Jsoup.connect("http://www.tonightsbedtimestory.com/stories/").userAgent(ConfigProvider.BOT_SETTINGS.userAgent())
                    .get().body().getElementsByAttributeValue("class", "post").forEach(element -> {
                String text = element.text();
                STORY_TITLES.add(text);
                String reduced = reduce(text);
                REDUCED_LIST.add(reduced);
                String link = element.html().substring(13);
                try {
                    STORY_MAP.put(reduced, Jsoup.connect(link.substring(0, link.indexOf("\""))).userAgent(ConfigProvider.BOT_SETTINGS.userAgent())
                            .get().body().getElementsByAttributeValue("class", "body").text());
                } catch (IOException e) {
                    throw new DevelopmentException("Could not load bedtime story " + text, e);
                }
            });
        } catch (IOException e) {
            throw new DevelopmentException("Could not load bedtime stories", e);
        }
    }
    @Command
    public void command(@Argument(optional = true, info = "story") String story, MessageMaker maker, VoiceChannel voiceChannel) {
        if (!story.isEmpty()) {
            String selected = StringHelper.getGoodMatch(reduce(story), REDUCED_LIST);
            if (selected == null) maker.append("I couldn't find that story");
            else{
                GuildAudioManager manager = GuildAudioManager.getManager(voiceChannel, true);
                manager.queueSpeech(new LangString(true, STORY_MAP.get(selected)));
                return;
            }
        }
        maker.getTitle().append("Here are the stories I can read to you");
        AtomicInteger perPageCount = new AtomicInteger(0);
        STORY_TITLES.forEach(s -> {
            maker.getNewListPart().appendRaw(s);
            if (perPageCount.incrementAndGet() % 11 == 10) maker.guaranteeNewListPage();
        });
    }
    private static String reduce(String s) {
        return FormatHelper.filtering(s, Character::isLetter).toLowerCase();
    }
}
