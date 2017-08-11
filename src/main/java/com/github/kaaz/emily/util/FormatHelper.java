package com.github.kaaz.emily.util;

import com.github.kaaz.emily.exeption.DevelopmentException;
import com.google.common.base.Strings;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Made by nija123098 on 3/18/2017.
 */
public class FormatHelper {
    public static String repeat(char c, int i) {
        char[] chars = new char[i];
        Arrays.fill(chars, c);
        return new String(chars);
    }
    public static String reduceRepeats(String s, char c){// use index of to optimize
        final StringBuilder builder = new StringBuilder();
        boolean repeat = false;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == c){
                if (!repeat){
                    builder.append(c);
                }
                repeat = true;
            }else{
                repeat = false;
                builder.append(s.charAt(i));
            }
        }
        return builder.toString();
    }
    public static String removeChars(String s, char toRemove){
        return filtering(s, c -> c != toRemove);
    }
    public static String trimFront(String s){
        for (int i = 0; i < s.length(); i++) if (s.charAt(i) != ' ') return s.substring(i);
        return "";
    }
    public static String makePleural(String s){
        return s + "'" + (s.endsWith("s") ? s + "" : "s");
    }
    public static int lengthOf(String[] args, int count){
        int l = 0;
        for (int i = 0; i < count; i++) {
            l += args[i].length();
        }
        return l;
    }
    public static String embedLink(String text, String link){
        return "[" + text + "](" + link + ")";
    }
    /**
     * @param headers array containing the headers
     * @param table   array[n size] of array's[header size], containing the rows of the controllers
     * @param footer
     * @return a formatted controllers
     */
    public static String makeAsciiTable(List<String> headers, List<List<String>> table, List<String> footer) {
        StringBuilder sb = new StringBuilder();
        int padding = 1;
        int[] widths = new int[headers.size()];
        Arrays.fill(widths, 0);
        for (int i = 0; i < headers.size(); i++) {
            if (headers.get(i).length() > widths[i]) {
                widths[i] = headers.get(i).length();
                if (footer != null) {
                    widths[i] = Math.max(widths[i], footer.get(i).length());
                }
            }
        }
        for (List<String> row : table) {
            for (int i = 0; i < row.size(); i++) {
                String cell = row.get(i);
                if (cell.length() > widths[i]) {
                    widths[i] = cell.length();
                }
            }
        }
        sb.append("```").append("\n");
        String formatLine = "|";
        for (int width : widths) {
            formatLine += " %-" + width + "s |";
        }
        formatLine += "\n";
        sb.append(appendSeparatorLine("+", "+", "+", padding, widths));
        sb.append(String.format(formatLine, headers.toArray()));
        sb.append(appendSeparatorLine("+", "+", "+", padding, widths));
        for (List<String> row : table) {
            sb.append(String.format(formatLine, row.toArray()));
        }
        if (footer != null) {
            sb.append(appendSeparatorLine("+", "+", "+", padding, widths));
            sb.append(String.format(formatLine, footer.toArray()));
        }
        sb.append(appendSeparatorLine("+", "+", "+", padding, widths));
        sb.append("```");
        return sb.toString();
    }

    /**
     * helper function for makeAsciiTable
     *
     * @param left    character on the left
     * @param middle  character in the middle
     * @param right   character on the right
     * @param padding controllers cell padding
     * @param sizes   width of each cell
     * @return a filler row for the controllers
     */
    private static String appendSeparatorLine(String left, String middle, String right, int padding, int... sizes) {
        boolean first = true;
        StringBuilder ret = new StringBuilder();
        for (int size : sizes) {
            if (first) {
                first = false;
                ret.append(left).append(Strings.repeat("-", size + padding * 2));
            } else {
                ret.append(middle).append(Strings.repeat("-", size + padding * 2));
            }
        }
        return ret.append(right).append("\n").toString();
    }

    /**
     * @param items items in the controllers
     * @return formatted controllers
     */
    public static String makeTable(List<String> items) {
        return makeTable(items, 16, 4);
    }

    /**
     * Makes a controllers-like display of list of items
     *
     * @param items        items in the controllers
     * @param columnLength length of a column(filled up with whitespace)
     * @param columns      amount of columns
     * @return formatted controllers
     */
    public static String makeTable(List<String> items, int columnLength, int columns) {
        String ret = "```\n";
        int counter = 0;
        for (String item : items) {
            counter++;
            ret += String.format("%-" + columnLength + "s", item);
            if (counter % columns == 0) {
                ret += "\n";
            }
        }
        if (counter % columns != 0) {
            ret += "\n";
        }
        return ret + "```\n";
    }

    private static final String DASH = EmoticonHelper.getChars("wavy_dash", true);
    public static String makeStackedBar(int max, int bar, String barChar) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bar; i++) {
            sb.append(barChar);
        }
        for (int i = bar; i < max; i++) {
            sb.append(DASH);
        }
        return sb.toString();
    }
    public static String getList(List<String> strings){
        switch (strings.size()) {
            case 0:
                throw new DevelopmentException("List provided is empty");
            case 1:
                return strings.get(0);
            case 2:
                return strings.get(0) + " and " + strings.get(1);
            default:
                String builder = "";
                for (int i = 0; i < strings.size() - 1; i++) {
                    builder += strings.get(i) + ", ";
                }
                return builder + "and " + strings.get(strings.size() - 1);
        }
    }
    public static String cleanOfXML(String s){
        StringBuilder builder = new StringBuilder();
        AtomicBoolean in = new AtomicBoolean();
        new StringIterator(s).forEachRemaining(character -> {
            if (character == '<') {
                in.set(true);
                return;
            }
            if (character == '>') {
                in.set(false);
                return;
            }
            if (!in.get()) builder.append(character);
        });
        return builder.toString();
    }
    public static List<String> cleanOfXML(List<String> strings){
        return strings.stream().map(FormatHelper::cleanOfXML).collect(Collectors.toList());
    }
    public static String filtering(String s, Function<Character, Boolean> filter){
        StringBuilder builder = new StringBuilder();
        new StringIterator(s).forEachRemaining(character -> {
            if (filter.apply(character)) builder.append(character);
        });
        return builder.toString();
    }
    public static String reformat(String s, Function<Character, Character> function){
        StringBuilder builder = new StringBuilder(s.length());
        for (int i = 0; i < s.length(); i++) {
            Character character = function.apply(s.charAt(i));
            if (character != null) builder.append(character);
        }
        return builder.toString();
    }
}
