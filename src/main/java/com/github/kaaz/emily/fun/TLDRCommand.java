package com.github.kaaz.emily.fun;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.util.*;
import com.google.common.base.Joiner;
import javafx.util.Pair;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Made by nija123098 on 7/15/2017.
 */
public class TLDRCommand extends AbstractCommand {
    private Map<String, Pair<String, String>> map;
    private List<String> entries;
    public TLDRCommand() {
        super("tldr", ModuleLevel.FUN, "tl;dr", null, "Shows a tldr from https://twitter.com/tldrwikipedia");
        List<Status> statuses = new ArrayList<>();
        int page = 0;
        while (true){
            try {
                ResponseList<Status> list = TwitterHelper.APPLICATION.getUserTimeline("tldrwikipedia", new Paging(++page, 200));
                if (list.size() != 200) break;
                statuses.addAll(list);
            } catch (TwitterException e) {
                Log.log("Eexception from Twitter", e);
            }
        }
        statuses.removeIf(status -> status.getText().startsWith("RT "));
        statuses.removeIf(status -> status.getMediaEntities().length < 1);
        this.map = new HashMap<>(statuses.size());
        Set<String> entries = new HashSet<>();
        String quote = Pattern.quote("(");
        statuses.forEach(status -> {
            String[] strings = status.getText().split(" ");
            String s = Joiner.on(" ").join(Stream.of(Arrays.copyOfRange(strings, 0, strings.length - 1)).filter(st -> !st.contains("/")).collect(Collectors.toList()));
            s = s.split(quote)[0].trim();
            StringBuilder builder = new StringBuilder();
            new StringIterator(s).forEachRemaining(character -> {
                if (Character.isLetter(character) || Character.isDigit(character) || character == ' ') builder.append(character);
            });
            String full = builder.toString();
            String reduced = FormatHelper.removeChars(full, ' ').toLowerCase();
            this.map.put(reduced, new Pair<>(full, status.getMediaEntities()[0].getMediaURL()));
            Stream.of(full.split(" ")).forEach(stri -> this.map.putIfAbsent(stri.toLowerCase(), new Pair<>(full, status.getMediaEntities()[0].getMediaURL())));
            entries.add(reduced);
        });
        this.entries = new ArrayList<>(entries);
    }
    @Command
    public void command(MessageMaker maker, String s) {
        if (s.isEmpty()) s = Rand.getRand(this.entries, false);
        else s = FormatHelper.removeChars(s, ' ').toLowerCase();
        Pair<String, String> pair = this.map.get(s);
        if (pair == null){
            maker.append("There is no an entry for that");
            return;
        }
        maker.getTitle().appendRaw(pair.getKey());
        maker.withImage(pair.getValue());
    }
}
