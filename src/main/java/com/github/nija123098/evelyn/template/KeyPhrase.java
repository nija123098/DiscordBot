package com.github.nija123098.evelyn.template;

import com.github.nija123098.evelyn.command.ContextRequirement;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

/**
 * A enum representation which defines a {@link CustomCommandDefinition}
 * and can be used to get a set of {@link Template}s for a specific
 * {@link Guild} or the entire bot it's self.
 *
 * @author nija123098
 * @since 1.0.0
 */
public enum KeyPhrase {
    TEST// no requirements or arguments for testing purposes
            (new ContextRequirement[0]),
    PLAY_TEXT//                       location
            (new ContextRequirement[]{ContextRequirement.SHARD}),
    USER_JOIN//                       location,                 location,                 joiner,                   first time,    favor
            (new ContextRequirement[]{ContextRequirement.GUILD, ContextRequirement.SHARD, ContextRequirement.USER}, Boolean.class, Float.class),
    USER_LEAVE//              location,                 location,                 kicker,                   target  invoker     kicked         banned
            (new ContextRequirement[]{ContextRequirement.GUILD, ContextRequirement.SHARD, ContextRequirement.USER}, User.class, Boolean.class, Boolean.class),
    REBOOT_NOTIFICATION//              location,                 location,                invoker
            (new ContextRequirement[]{ContextRequirement.GUILD, ContextRequirement.SHARD, ContextRequirement.USER}),;
    private CustomCommandDefinition definition;
    KeyPhrase(ContextRequirement[] availableContext, Class<?>...argTypes) {
        this.definition = new CustomCommandDefinition(this.name(), availableContext, argTypes);
    }
    public CustomCommandDefinition getDefinition(){
        return this.definition;
    }
}
