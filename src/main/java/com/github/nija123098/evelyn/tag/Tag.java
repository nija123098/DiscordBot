package com.github.nija123098.evelyn.tag;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public enum Tag {
    HELPFUL(new Color(39, 209, 110)),
    ARCADE(new Color(54, 57, 62));
    private final Color color;
    private final List<Tagable> taged = new ArrayList<>(1);
    Tag(Color color) {
        this.color = color;
    }
    private static void register(Tagable tagable) {
        tagable.getTags().forEach(tag -> tag.taged.add(tagable));
    }

    public Color getColor() {
        return this.color;
    }
}
