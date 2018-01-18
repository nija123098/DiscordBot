package com.github.nija123098.evelyn.util;

import com.fasterxml.jackson.databind.util.ArrayIterator;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import static com.github.nija123098.evelyn.util.FormatHelper.filtering;
import static com.google.api.client.util.Joiner.on;
import static com.mashape.unirest.http.Unirest.get;
import static java.lang.Double.MAX_VALUE;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.util.Arrays.copyOfRange;
import static org.apache.commons.lang3.StringUtils.getJaroWinklerDistance;
import static org.apache.commons.lang3.StringUtils.getLevenshteinDistance;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class StringHelper {
    public static String getFileType(String url) {
        StringBuilder builder = new StringBuilder();
        for (int i = url.length() - 1; i > -1; --i) {
            if (url.charAt(i) == '.') break;
            else builder.append(url.charAt(i));
        }
        return builder.reverse().toString();
    }

    public static String ensureSize(String string, int size) {
        if (string.length() <= size) return string;
        else return string.substring(0, size - 3) + "...";
    }

    public static String readAll(String url) throws IOException, UnirestException {
        return get(url).asString().getBody();
    }

    public static String getGoodMatch(String in, List<String> strings) {
        List<String> removing = new ArrayList<>(strings);
        removing.removeIf(s -> !s.toLowerCase().contains(in.toLowerCase()));
        if (removing.size() == 1 && acceptableDistance(removing.get(0), in)) return removing.get(0);
        else if (!removing.isEmpty()) strings = removing;
        List<String> best = getGoodMatch(in, strings, StringUtils::getLevenshteinDistance, true, true);
        best = getGoodMatch(in, best, StringUtils::getJaroWinklerDistance, true, true);
        if (best.size() > 1) {
            best = getGoodMatch(in, best, StringUtils::getLevenshteinDistance, true, false);
        }
        best.removeIf(s -> getJaroWinklerDistance(in, s) == 0);
        return best.size() != 1 || !acceptableDistance(best.get(0), in) ? null : best.get(0);
    }

    public static boolean acceptableDistance(String best, String in) {
        best = best.toLowerCase();
        in = in.toLowerCase();
        return getLevenshteinDistance(best, in) < .25F * max(max(in.length(), best.length()), 6) || getLevenshteinDistance(best, filtering(in, Character::isLetter)) < .25F * max(in.length(), best.length());
    }

    public static List<String> getGoodMatch(String matching, List<String> candidates, BiFunction<String, String, Number> function, boolean golf, boolean containment) {
        double bestScore = MAX_VALUE;
        List<String> best = new ArrayList<>();
        Map<Integer, String> splitting = new HashMap<>();
        String match;
        for (String s : candidates) {
            match = splitting.computeIfAbsent(s.split(" ").length, integer -> {
                String[] matchingSplit = matching.split(" ");
                return on(' ').join(new ArrayIterator<>(copyOfRange(matchingSplit, 0, min(integer, matchingSplit.length))));
            });
            if ((match.toLowerCase().contains(s.toLowerCase()) || s.toLowerCase().contains(match.toLowerCase())) && containment) {
                if (golf ? bestScore > .001 : bestScore < .001) best.clear();
                bestScore = .001;
                best.add(s);
            } else {
                double score = function.apply(match, s).doubleValue();
                if (bestScore == score) best.add(s);
                else if (golf ? bestScore > score : bestScore < score) {
                    best.clear();
                    best.add(s);
                    bestScore = score;
                }
            }
        }
        return best;
    }

    public static int instances(String s, char c) {
        int count = 0;
        for (int i = 0; i < s.length(); i++) if (s.charAt(i) == c) ++count;
        return count;
    }

    public static String join(String[] args, String splitter, int start, int length) {
        StringBuilder builder = new StringBuilder();
        length += start - 1;
        for (int i = start; i < length; i++) {
            builder.append(args[i]);
            builder.append(splitter);
        }
        return builder.append(args[length]).toString();
    }
}
