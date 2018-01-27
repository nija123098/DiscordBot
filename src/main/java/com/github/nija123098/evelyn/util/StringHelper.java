package com.github.nija123098.evelyn.util;

import com.google.common.base.Joiner;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.commons.text.similarity.JaroWinklerDistance;
import org.apache.commons.text.similarity.LevenshteinDistance;

import java.util.*;
import java.util.function.BiFunction;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class StringHelper {
    private static final LevenshteinDistance LEVENSHTEIN_DISTANCE = new LevenshteinDistance();
    private static final JaroWinklerDistance JARO_WINKLER_DISTANCE = new JaroWinklerDistance();

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

    public static String readAll(String url) throws UnirestException {
        return Unirest.get(url).asString().getBody();
    }

    public static String getGoodMatch(String in, List<String> strings) {
        List<String> removing = new ArrayList<>(strings);
        removing.removeIf(s -> !s.toLowerCase().contains(in.toLowerCase()));
        if (removing.size() == 1 && acceptableDistance(removing.get(0), in)) return removing.get(0);
        else if (!removing.isEmpty()) strings = removing;
        List<String> best = getGoodMatch(in, strings, LEVENSHTEIN_DISTANCE::apply, true, true);
        best = getGoodMatch(in, best, JARO_WINKLER_DISTANCE::apply, true, true);
        if (best.size() > 1) {
            best = getGoodMatch(in, best, LEVENSHTEIN_DISTANCE::apply, true, false);
        }
        best.removeIf(s -> JARO_WINKLER_DISTANCE.apply(in, s) == 0);
        return best.size() != 1 || !acceptableDistance(best.get(0), in) ? null : best.get(0);
    }

    public static boolean acceptableDistance(String best, String in) {
        best = best.toLowerCase();
        in = in.toLowerCase();
        return LEVENSHTEIN_DISTANCE.apply(best, in) < .25F * Math.max(Math.max(in.length(), best.length()), 6) || LEVENSHTEIN_DISTANCE.apply(best, FormatHelper.filtering(in, Character::isLetter)) < .25F * Math.max(in.length(), best.length());
    }

    public static List<String> getGoodMatch(String matching, List<String> candidates, BiFunction<String, String, Number> function, boolean golf, boolean containment) {
        double bestScore = Integer.MAX_VALUE;
        List<String> best = new ArrayList<>();
        Map<Integer, String> splitting = new HashMap<>();
        String match;
        for (String s : candidates) {
            match = splitting.computeIfAbsent(s.split(" ").length, integer -> {
                String[] matchingSplit = matching.split(" ");
                return Joiner.on(' ').join(Arrays.copyOfRange(matchingSplit, 0, Math.min(integer, matchingSplit.length)));
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
