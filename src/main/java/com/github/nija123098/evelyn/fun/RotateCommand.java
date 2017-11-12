package com.github.nija123098.evelyn.fun;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.exeption.ArgumentException;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Made by nija123098 on 5/22/2017.
 */
public class RotateCommand extends AbstractCommand {
    private static final BidiMap<String, String> CHAR_MAP;
    static {
        CHAR_MAP = new DualHashBidiMap<>();
        CHAR_MAP.put("a", "\u0250");
        CHAR_MAP.put("b", "q");
        CHAR_MAP.put("c", "\u0254");
        CHAR_MAP.put("d", "p");
        CHAR_MAP.put("e", "\u01DD");
        CHAR_MAP.put("f", "\u025F");
        CHAR_MAP.put("g", "\u0183");
        CHAR_MAP.put("h", "\u0265");
        CHAR_MAP.put("i", "\u1D09");
        CHAR_MAP.put("j", "\u027E");
        CHAR_MAP.put("k", "\u029E");
        CHAR_MAP.put("m", "\u026F");
        CHAR_MAP.put("n", "u");
        CHAR_MAP.put("r", "\u0279");
        CHAR_MAP.put("t", "\u0287");
        CHAR_MAP.put("v", "\u028C");
        CHAR_MAP.put("w", "\u028D");
        CHAR_MAP.put("y", "\u028E");
        CHAR_MAP.put("A", "\u2200");
        CHAR_MAP.put("C", "\u0186");
        CHAR_MAP.put("E", "\u018E");
        CHAR_MAP.put("F", "\u2132");
        CHAR_MAP.put("G", "\u05E4");
        CHAR_MAP.put("J", "\u017F");
        CHAR_MAP.put("L", "\u02E5");
        CHAR_MAP.put("M", "W");
        CHAR_MAP.put("N", "N");
        CHAR_MAP.put("P", "\u0500");
        CHAR_MAP.put("T", "\u2534");
        CHAR_MAP.put("U", "\u2229");
        CHAR_MAP.put("V", "\u039B");
        CHAR_MAP.put("Y", "\u2144");
        CHAR_MAP.put("1", "\u0196");
        CHAR_MAP.put("2", "\u1105");
        CHAR_MAP.put("3", "\u0190");
        CHAR_MAP.put("4", "\u3123");
        CHAR_MAP.put("5", "\u03DB");
        CHAR_MAP.put("6", "9");
        CHAR_MAP.put("7", "\u3125");
        CHAR_MAP.put("9", "6");
        CHAR_MAP.put(".", "\u02D9");
        CHAR_MAP.put(",", " '");
        CHAR_MAP.put("'", ",");
        CHAR_MAP.put("\"", ",,");
        CHAR_MAP.put("`", ",");
        CHAR_MAP.put("?", "\u00BF");
        CHAR_MAP.put("!", "\u00A1");
        CHAR_MAP.put("[", "]");
        CHAR_MAP.put("]", "[");
        CHAR_MAP.put("(", ")");
        CHAR_MAP.put(")", "(");
        CHAR_MAP.put("{", "}");
        CHAR_MAP.put("}", "{");
        CHAR_MAP.put("<", ">");
        CHAR_MAP.put(">", "<");
        CHAR_MAP.put("&", "\u214B");
        CHAR_MAP.put("_", "\u203E");
        CHAR_MAP.put("\u2234", "\u2235");
        CHAR_MAP.put("\u2045", "\u2046");
    }
    public RotateCommand() {
        super("rotate", ModuleLevel.FUN, null, null, "Rotate text!");
    }
    @Command
    public void command(MessageMaker maker, @Argument(info = "The text to rotate") String arg){
        if (arg == null || arg.isEmpty()) throw new ArgumentException("Please give text for me to rotate");
        StringBuilder builder = new StringBuilder(arg.length());
        Stream.of(arg.split("")).forEach(s -> {
            String st = CHAR_MAP.get(s);
            if (st == null) st = CHAR_MAP.getKey(s);
            if (st == null) st = s;
            builder.append(st);
        });
        builder.reverse();
        maker.appendRaw(builder.toString());
    }
}
