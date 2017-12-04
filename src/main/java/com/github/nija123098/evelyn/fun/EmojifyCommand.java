package com.github.nija123098.evelyn.fun;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.exception.ArgumentException;
import com.github.nija123098.evelyn.util.EmoticonHelper;
import com.github.nija123098.evelyn.util.StringIterator;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;

import static com.github.nija123098.evelyn.command.ModuleLevel.FUN;
import static com.github.nija123098.evelyn.util.EmoticonHelper.getAll;
import static java.lang.Character.*;
import static java.lang.String.valueOf;
import static java.util.stream.Stream.of;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class EmojifyCommand extends AbstractCommand {
    private static final Map<String, String> EXTRAPOLATED = new HashMap<>();

    static {
        Map<String, Pair<Integer, String>> map = new HashMap<>();
        getAll().forEach((s, strings) -> strings.forEach(string -> of(string.split("_")).forEach(split -> map.compute(split, (sp, pair) -> pair == null || pair.getKey() > split.length() ? new Pair<>(split.length(), s) : pair))));
        map.forEach((s, pair) -> EXTRAPOLATED.put(s, pair.getValue()));
    }

    public EmojifyCommand() {
        super("emojify", FUN, null, null, "Turns you input into emojies");
    }

    @Command
    public void command(String args, MessageMaker maker) {
        if (args.isEmpty()) throw new ArgumentException("Please insert some text for me to emojify");
        StringBuilder builder = new StringBuilder();
        of(args.split(" ")).forEach(word -> {
            String extrapolation = EXTRAPOLATED.get(word);
            if (extrapolation == null)
                new StringIterator(word).forEachRemaining(character -> builder.append(getChars(character)));
            else builder.append(extrapolation);
        });
        maker.appendRaw(builder.toString());
    }

    private String getChars(char c) {
        if (isLetter(c))
            return EmoticonHelper.getChars("regional_indicator_" + toLowerCase(c), true);
        if (isDigit(c)) return EmoticonHelper.getChars(Character.getName(c).substring(6).toLowerCase(), true);
        if (c == '?') return EmoticonHelper.getChars("grey_question", true);
        if (c == '!') return EmoticonHelper.getChars("grey_exclamation", true);
        return valueOf(c);
    }
}
