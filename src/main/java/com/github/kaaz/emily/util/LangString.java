package com.github.kaaz.emily.util;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Made by nija123098 on 3/17/2017.
 */
public class LangString {
    private final List<Pair<Boolean, String>> value = new ArrayList<>();
    public LangString() {
    }
    public LangString(boolean translate, String content) {
        append(translate, content);
    }
    public LangString append(boolean translate, String content) {
        value.add(new Pair<>(translate, content));
        return this;
    }
    public LangString appendTranslation(String content) {
        value.add(new Pair<>(true, content));
        return this;
    }
    public LangString appendRaw(String content) {
        value.add(new Pair<>(false, content));
        return this;
    }
    public LangString appendToggle(boolean rawFirst, String...content) {
        for (String c : content) {
            rawFirst = !rawFirst;
            append(rawFirst, c);
        }
        return this;
    }
    public String translate(String lang) {
        final StringBuilder builder = new StringBuilder();
        value.forEach(pair -> builder.append(pair.getKey() ? TranslateHelper.translate(lang, pair.getValue()) : pair.getValue()));
        return builder.toString();
    }
    public String asBuilt() {
        final StringBuilder builder = new StringBuilder();
        value.forEach(pair -> builder.append(pair.getValue()));
        return builder.toString();
    }
}
