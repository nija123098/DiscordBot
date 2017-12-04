package com.github.nija123098.evelyn.util;

import java.util.Iterator;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class StringIterator implements Iterator<Character> {
    private int i;
    private String string;

    public StringIterator(String string) {
        this.string = string;
    }

    @Override
    public boolean hasNext() {
        return this.i != this.string.length();
    }

    @Override
    public Character next() {
        if (!hasNext()) throw new RuntimeException("No next character in the StringIterator");
        return this.string.charAt(this.i++);
    }
}
