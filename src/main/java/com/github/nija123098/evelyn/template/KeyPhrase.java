package com.github.nija123098.evelyn.template;

import com.github.nija123098.evelyn.command.ContextRequirement;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

/**
 * Made by nija123098 on 4/17/2017.
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
    ADVANCED_TESTING(new ContextRequirement[]{ContextRequirement.CHANNEL, ContextRequirement.MESSAGE}, User.class),;
    private CustomCommandDefinition definition;
    KeyPhrase(ContextRequirement[] availableContext, Class<?>...argTypes) {
        this.definition = new CustomCommandDefinition(this.name(), availableContext, argTypes);
    }
    public CustomCommandDefinition getDefinition(){
        return this.definition;
    }
}
