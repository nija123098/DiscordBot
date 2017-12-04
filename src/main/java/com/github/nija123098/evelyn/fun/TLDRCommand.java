package com.github.nija123098.evelyn.fun;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.exception.DevelopmentException;
import com.github.nija123098.evelyn.util.StringIterator;
import javafx.util.Pair;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;

import java.awt.*;
import java.util.*;
import java.util.List;

import static com.github.nija123098.evelyn.botconfiguration.ConfigProvider.AUTH_KEYS;
import static com.github.nija123098.evelyn.command.ModuleLevel.FUN;
import static com.github.nija123098.evelyn.util.FormatHelper.removeChars;
import static com.github.nija123098.evelyn.util.Log.log;
import static com.github.nija123098.evelyn.util.LogColor.red;
import static com.github.nija123098.evelyn.util.Rand.getRand;
import static com.github.nija123098.evelyn.util.TwitterHelper.APPLICATION;
import static com.google.common.base.Joiner.on;
import static com.google.common.base.Objects.equal;
import static java.lang.Character.isDigit;
import static java.lang.Character.isLetter;
import static java.util.Arrays.copyOfRange;
import static java.util.regex.Pattern.quote;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.of;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class TLDRCommand extends AbstractCommand {
    private Map<String, Pair<String, String>> map;
    private List<String> entries;

    public TLDRCommand() {
        super("tldr", FUN, "tl;dr", null, "Shows a tldr from https://twitter.com/tldrwikipedia");
        //if twitter token not found, do nothing
        if (equal(AUTH_KEYS.twitter_secret(), "na")) {
            log(red("Could not load TLDR command. Token not found."));
            return;
        }
        List<Status> statuses = new ArrayList<>();
        int page = 0;
        while (true) {
            try {
                ResponseList<Status> list = APPLICATION.getUserTimeline("tldrwikipedia", new Paging(++page, 200));
                if (list.size() != 200) break;
                statuses.addAll(list);
            } catch (TwitterException e) {
                throw new DevelopmentException("Exception getting tl;dr data from twitter", e);
            }
        }
        statuses.removeIf(status -> status.getText().startsWith("RT "));
        statuses.removeIf(status -> status.getMediaEntities().length < 1);
        this.map = new HashMap<>(statuses.size());
        Set<String> entries = new HashSet<>();
        String quote = quote("(");
        statuses.forEach(status -> {
            String[] strings = status.getText().split(" ");
            String s = on(" ").join(of(copyOfRange(strings, 0, strings.length - 1)).filter(st -> !st.contains("/")).collect(toList()));
            s = s.split(quote)[0].trim();
            StringBuilder builder = new StringBuilder();
            new StringIterator(s).forEachRemaining(character -> {
                if (isLetter(character) || isDigit(character) || character == ' ')
                    builder.append(character);
            });
            String full = builder.toString();
            String reduced = removeChars(full, ' ').toLowerCase();
            this.map.put(reduced, new Pair<>(full, status.getMediaEntities()[0].getMediaURL()));
            of(full.split(" ")).forEach(stri -> this.map.putIfAbsent(stri.toLowerCase(), new Pair<>(full, status.getMediaEntities()[0].getMediaURL())));
            entries.add(reduced);
        });
        this.entries = new ArrayList<>(entries);
    }

    @Command
    public void command(MessageMaker maker, String s) {
        try {
            if (s.isEmpty()) s = getRand(this.entries, false);
            else s = removeChars(s, ' ').toLowerCase();
            Pair<String, String> pair = this.map.get(s);
            if (pair == null) {
                maker.append("There is no an entry for that");
                return;
            }
            maker.getTitle().appendRaw(pair.getKey());
            maker.withImage(pair.getValue());
        } catch (NullPointerException e) {
            if (equal(AUTH_KEYS.twitter_secret(), "na")) {
                maker.mustEmbed().withColor(new Color(255, 0, 0));
                maker.getHeader().clear().append("Sorry, we are in the process of updating our API key!");
            } else {
                log("TLDR Could not load map.", e);
            }
        }
    }
}