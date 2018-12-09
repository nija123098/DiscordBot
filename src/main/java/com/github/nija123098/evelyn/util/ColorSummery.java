package com.github.nija123098.evelyn.util;

import java.awt.image.BufferedImage;
import java.util.Objects;
import java.util.regex.Pattern;

public class ColorSummery {
    private static final String SPLITTER = ":";
    private static final String SPLITTER_QUOTE = Pattern.quote(SPLITTER);
    private final int red, green, blue;
    public ColorSummery(long red, long green, long blue) {
        this.red = (int) red;
        this.green = (int) green;
        this.blue = (int) blue;
    }

    public ColorSummery(BufferedImage image, int x, int y, int xEnd, int yEnd) {
        long r = 0, g = 0, b = 0;
        int val;
        for (int i = x; i < xEnd; ++i) {
            for (int j = y; j < yEnd; ++j) {
                val = image.getRGB(i, j);
                r += (val & 0xFF0000) >> 16;
                g += (val & 0xFF00) >> 8;
                b += val & 0xFF;
            }
        }
        int weight = (xEnd - x) * (yEnd - y);
        this.red = (int) (r / weight);
        this.green = (int) (g / weight);
        this.blue = (int) (b / weight);
    }

    public ColorSummery(BufferedImage image) {
        this(image, 0, 0, image.getWidth(), image.getHeight());
    }

    public ColorSummery(String val) {
        String[] split = val.split(SPLITTER_QUOTE);
        this.red = Integer.parseInt(split[0]);
        this.green = Integer.parseInt(split[1]);
        this.blue = Integer.parseInt(split[2]);
    }

    public int getRed() {
        return this.red;
    }

    public int getGreen() {
        return this.green;
    }

    public int getBlue() {
        return this.blue;
    }

    public double compare(ColorSummery other) {
        if (other == null) throw new NullPointerException("Comparison can not be done with a null");
        // int rmean = (this.red + other.red) >> 1;
        // return (int) (((int) ((512+rmean) * Math.pow(this.red - other.red, 2)) >> 8) + 4 * Math.pow(this.green - other.green, 2) + ((int) ((767-rmean) * Math.pow(this.blue - other.blue, 2)) >> 8));
        return Math.pow(this.red - other.red, 2) + Math.pow(this.green - other.green, 2) + Math.pow(this.blue - other.blue, 2);
    }

    @Override
    public String toString() {
        return this.red + SPLITTER + this.green + SPLITTER + this.blue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ColorSummery that = (ColorSummery) o;
        return this.red == that.red &&
                this.green == that.green &&
                this.blue == that.blue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.red, this.green, this.blue);
    }
}
