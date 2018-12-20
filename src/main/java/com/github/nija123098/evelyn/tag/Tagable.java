package com.github.nija123098.evelyn.tag;

import java.util.List;

public interface Tagable {
    /**
     * Returns the name of the instance.
     *
     * @return the name of the instance.
     */
    String getName();

    /**
     * Returns the name of the type of tagable.
     *
     * @return the name of the type of tagable.
     */
    String typeName();

    /**
     * Returns the tags that this instance has.
     *
     * @return the tags that this instance has.
     */
    List<Tag> getTags();
}
