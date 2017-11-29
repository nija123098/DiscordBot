package com.github.nija123098.evelyn.util;

import com.github.nija123098.evelyn.exception.ArgumentException;

import java.awt.*;
import java.util.List;

/**
 * @author nija123098
 */
public class ColorRange {
    private final Color[] colors;
    public ColorRange(Color...colors) {
        this.colors = colors;
    }
    public ColorRange(List<Color> colors) {
        this.colors = colors.toArray(new Color[colors.size()]);
    }
    public Color getColor(float value){
        if (value > 1 || value < 0) throw new ArgumentException("The gradient value must be 0 <= value <= 1");
        int val = (int) (value * (this.colors.length - 1) * .999F);
        return GraphicsHelper.getGradient(1 - ((value * (this.colors.length - 1)) - val), this.colors[val], this.colors[val + 1]);
    }
}
