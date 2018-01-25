package com.github.nija123098.evelyn.util;

import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.Role;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.exception.DevelopmentException;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class FormatHelper {
    public static String repeat(char c, int i) {
        char[] chars = new char[i];
        Arrays.fill(chars, c);
        return new String(chars);
    }

    public static String reduceRepeats(String s, char c) {// use index of to optimize
        final StringBuilder builder = new StringBuilder();
        boolean repeat = false;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == c) {
                if (!repeat) {
                    builder.append(c);
                }
                repeat = true;
            } else {
                repeat = false;
                builder.append(s.charAt(i));
            }
        }
        return builder.toString();
    }

    public static String removeChars(String s, char toRemove) {
        return filtering(s, c -> c != toRemove);
    }

    public static String trimFront(String s) {
        for (int i = 0; i < s.length(); i++) if (s.charAt(i) != ' ') return s.substring(i);
        return "";
    }

    public static String makePleural(String s) {
        return s + "'" + (s.endsWith("s") ? s + "" : "s");
    }

    public static int lengthOf(String[] args, int count) {
        int l = 0;
        for (int i = 0; i < count; i++) {
            l += args[i].length();
        }
        return l;
    }

    public static String embedLink(String text, String link) {
        return "[" + text + "](" + (link.isEmpty() ? ConfigProvider.URLS.rickrollVid() : link) + ")";
    }

    /**
     *
     *
     * @param headers array containing the headers
     * @param table   array[n size] of array's[header size], containing the rows of the controllers
     * @param footer
     * @return a formatted controllers
     */
    public static String makeAsciiTable(List<String> headers, List<List<String>> table, List<String> footer) {// ----------------------------------------
        List<Integer> widths = Stream.concat(Stream.of(headers, footer), table.stream()).filter(Objects::nonNull).map(strings -> strings.stream().map(String::length).collect(Collectors.toList())).reduce((integers, integers2) -> {
            for (int i = 0; i < integers2.size(); i++) integers.set(i, Math.max(integers.get(i), integers2.get(i)));
            return integers;
        }).get();
        StringBuilder builder = new StringBuilder("```\n");
        String midBar = "+" + widths.stream().map(integer -> repeat('-', integer + 2)).reduce((s, s2) -> s + "+" + s2).get() + "+\n";
        builder.append(midBar);
        formatContents(builder, widths, headers);
        builder.append(midBar);
        table.forEach(strings -> formatContents(builder, widths, strings));
        builder.append(midBar);
        if (footer != null) {
            formatContents(builder, widths, footer);
            builder.append(midBar);
        }
        return builder.append("```").toString();
    }

    private static void formatContents(StringBuilder builder, List<Integer> widths, List<String> contents){
        builder.append("| ");
        for (int i = 0; i < widths.size(); i++) {
            builder.append(contents.get(i)).append(repeat(' ', widths.get(i) - contents.get(i).length()));
            if (i != widths.size() - 1) builder.append(" | ");
        }
        builder.append(" |\n");
    }

    /**
     * @param items items in the controllers
     * @return formatted controllers
     */
    public static String makeTable(List<String> items) {
        int length = items.stream().map(String::length).reduce(Math::max).orElse(0) + 3;
        StringBuilder builder = new StringBuilder("```\n");
        AtomicInteger currentLength = new AtomicInteger();
        items.forEach(s -> {
            if (currentLength.get() + s.length() > 64 && currentLength.get() != 0){
                builder.append("\n");
                currentLength.set(0);
            }
            builder.append(s).append(repeat(' ', length - s.length()));
        });
        return builder.append("\n```").toString();
    }

    /**
     * Makes a formatted table of user permissions
     *
     * @param user the user to get the permissions table for.
     * @param guild the guild to get the user's permissions table for.
     * @param simple if the table should be in a simple format.
     * @return the formatted permissions table.
     */
    public static String makeUserPermissionsTable(User user, Guild guild, boolean simple) {
        List<String> permissions = new ArrayList<>();
        user.getPermissionsForGuild(guild).forEach(permission -> {
            if (permission.name().contains("MANAGE")) {
                permissions.add("'" + permission.name().toLowerCase() + "'");
            } else if (permission.name().contains("KICK") || permission.name().contains("BAN") || permission.name().contains("MOVE") || permission.name().contains("DEAFEN") || permission.name().contains("MUTE") || permission.name().contains("LOG")) {
                permissions.add('"' + permission.name().toLowerCase() + '"');
            } else if (permission.name().contains("ADMINISTRATOR")){
                permissions.add(" Administrator ");
            } else if (!simple) {
                permissions.add(" " + permission.name().toLowerCase() + " ");
            }
        });
        Collections.sort(permissions);
        StringBuilder ret = new StringBuilder("```ml\n");
        int counter = 0;
        for (String item : permissions) {
            counter++;
            ret.append(String.format("%-" + 23 + "s", item));
            if (counter % 2 == 0) {
                ret.append("\n");
            }
        }
        if (counter % 2 != 0) {
            ret.append("\n");
        }
        return ret + "```\n";
    }

    /**
     * Makes a formatted table of role permissions.
     *
     * @param role the role to get the permissions table for.
     * @return the formatted permissions table.
     */
    public static String makeRolePermissionsTable(Role role) {
        List<String> permissions = new ArrayList<>();
        role.getPermissions().forEach(permission -> {
            if (permission.name().contains("MANAGE") || permission.name().contains("LOG")) {
                permissions.add("'" + permission.name().toLowerCase() + "'");
            } else if (permission.name().contains("KICK") || permission.name().contains("BAN") || permission.name().contains("MOVE") || permission.name().contains("DEAFEN") || permission.name().contains("MUTE")) {
                permissions.add('"' + permission.name().toLowerCase() + '"');
            } else if (permission.name().contains("ADMINISTRATOR")){
                permissions.add(" Administrator ");
            } else {
                permissions.add(" " + permission.name().toLowerCase() + " ");
            }
        });
        Collections.sort(permissions);
        StringBuilder ret = new StringBuilder("```ml\n");
        int counter = 0;
        for (String item : permissions) {
            counter++;
            ret.append(String.format("%-" + 23 + "s", item));
            if (counter % 2 == 0) {
                ret.append("\n");
            }
        }
        if (counter % 2 != 0) {
            ret.append("\n");
        }
        return ret + "```\n";
    }

    /**
     * Makes a controllers-like display of list of items.
     *
     * @param items        the items in the controllers.
     * @param columnLength the length of a column filled with whitespace.
     * @param columns      the amount of columns.
     * @return the formatted controller-like display.
     */
    public static String makeUserTable(List<User> items, int columnLength, int columns) {
        StringBuilder ret = new StringBuilder("```\n");
        int counter = 0;
        for (User item : items) {
            counter++;
            ret.append(String.format("%-" + columnLength + "s", item.getName()));
            if (counter % columns == 0) {
                ret.append("\n");
            }
        }
        if (counter % columns != 0) {
            ret.append("\n");
        }
        return ret + "```\n";
    }

    public static String getList(List<String> strings) {
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

    public static String cleanOfXML(String s) {
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

    public static List<String> cleanOfXML(List<String> strings) {
        return strings.stream().map(FormatHelper::cleanOfXML).collect(Collectors.toList());
    }

    public static String filtering(String s, Function<Character, Boolean> filter) {
        StringBuilder builder = new StringBuilder();
        new StringIterator(s).forEachRemaining(character -> {
            if (filter.apply(character)) builder.append(character);
        });
        return builder.toString();
    }

    public static String reformat(String s, Function<Character, Character> function) {
        StringBuilder builder = new StringBuilder(s.length());
        for (int i = 0; i < s.length(); i++) {
            Character character = function.apply(s.charAt(i));
            if (character != null) builder.append(character);
        }
        return builder.toString();
    }

    public static String reduce(String s) {
        return filtering(s, Character::isLetter);
    }

    public static Set<String> reduce(Set<String> strings) {
        return strings.stream().map(FormatHelper::reduce).collect(Collectors.toSet());
    }

    public static String addComas(double l) {
        String str = String.valueOf(l);
        if (str.endsWith(".0")) str = str.substring(0, str.length() - 2);
        int eIndex = str.indexOf("E");
        if (eIndex != -1) return str.substring(0, 4) + str.substring(eIndex);
        if (l < 9999) return str;
        StringBuilder builder = new StringBuilder();
        int bound = l % 1 == 0 ? str.length() : str.indexOf(".");
        for (int i = bound - 1; i > -1; --i) {
            builder.append(str.charAt(bound - i - 1));
            if (i % 3 == bound % 3 && i != 0) builder.append(",");
        }
        if (bound != str.length()) {
            for (int i = bound; i < str.length(); i++) {
                builder.append(str.charAt(i));
            }
        }
        return builder.toString();
    }

    public static String addComas(String bigInteger) {
        if (bigInteger.length() < 5) return bigInteger;
        int decimal = bigInteger.indexOf(".");
        if (decimal != -1) bigInteger = bigInteger.substring(0, decimal);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < bigInteger.length(); i++) {
            if (i % 3 == bigInteger.length() % 3 && i != 0) builder.append(",");
            builder.append(bigInteger.charAt(i));
        }
        return builder.toString();
    }

    public static String limitLength(String s,int length){
        return s.length() > length ? s.substring(0, length) : s;
    }
}