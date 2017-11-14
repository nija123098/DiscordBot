package com.github.nija123098.evelyn.util;

import org.apache.commons.lang3.StringUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * Written by Soarnir 13/11/17
 */

public class TableBuilder {

    List<String[]> rows = new LinkedList<>();

    public void addRow(String... cols) {
        rows.add(cols);
    }

    private int[] colWidths() {
        int cols = -1;
        for(String[] row : rows)
            cols = Math.max(cols, row.length);
        int[] widths = new int[cols];
        for(String[] row : rows) {
            for(int colNum = 0; colNum < row.length; colNum++) {
                widths[colNum] = Math.max(widths[colNum], StringUtils.length(row[colNum]));
            }
        }
        return widths;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        int[] colWidths = colWidths();
        for(String[] row : rows) {
            for(int colNum = 0; colNum < row.length; colNum++) {
                buf.append("\u200b");
                if (row[colNum].contains("**")) {
                    buf.append("`|` **`" + StringUtils.rightPad(StringUtils.defaultString(row[colNum].substring(2, row[colNum].length() - 2)), colWidths[colNum]) + " \u200b`**");
                    buf.append("\u200b");
                } else {
                    buf.append("`|` `" + StringUtils.rightPad(StringUtils.defaultString(row[colNum]), colWidths[colNum]) + " \u200b`");
                    buf.append("\u200b");
                }
            }
            buf.append("\u200b`|`\n");
        }
        return buf.toString();
    }


}
