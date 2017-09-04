package com.github.kaaz.emily.fun;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.exeption.ArgumentException;
import com.github.kaaz.emily.util.EmoticonHelper;
import com.github.kaaz.emily.util.Log;
import com.github.kaaz.emily.util.StringIterator;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Made by nija123098 on 5/11/2017.
 */
public class EmojifyCommand extends AbstractCommand {
    private static final Map<String, String> EXTRAPOLATED = new HashMap<>();
    static {
        Map<String, Pair<Integer, String>> map = new HashMap<>();
        EmoticonHelper.getAll().forEach((s, strings) -> strings.forEach(string -> Stream.of(string.split("_")).forEach(split -> map.compute(split, (sp, pair) -> pair == null || pair.getKey() > split.length() ? new Pair<>(split.length(), s) : pair))));
        map.forEach((s, pair) -> EXTRAPOLATED.put(s, pair.getValue()));
    }
    public EmojifyCommand() {
        super("emojify", ModuleLevel.FUN, null, null, "Turns you input into emojies");
    }
    @Command
    public void command(String args, MessageMaker maker){
        if (args.isEmpty()) throw new ArgumentException("Please insert some text for me to emojify");
        StringBuilder builder = new StringBuilder();
        Stream.of(args.split(" ")).forEach(word -> {
            String extrapolation = EXTRAPOLATED.get(word);
            if (extrapolation == null) new StringIterator(word).forEachRemaining(character -> builder.append(getChars(character)));
            else builder.append(extrapolation);
        });
        maker.appendRaw(builder.toString());
    }
    private String getChars(char c){
        if (Character.isLetter(c)) return EmoticonHelper.getChars("regional_indicator_" + Character.toLowerCase(c), true);
        if (Character.isDigit(c)) return EmoticonHelper.getChars(Character.getName(c).substring(6).toLowerCase(), true);
        if (c == '?') return EmoticonHelper.getChars("grey_question", true);
        if (c == '!') return EmoticonHelper.getChars("grey_exclamation", true);
        return String.valueOf(c);
    }
}
