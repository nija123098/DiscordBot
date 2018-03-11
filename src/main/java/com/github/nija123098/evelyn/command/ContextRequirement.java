package com.github.nija123098.evelyn.command;

import com.github.nija123098.evelyn.discordobjects.wrappers.*;

/**
 * Enum representations for possible sources of context
 * in order to mark if a command requires some context
 * in it's invocation context.  This is useful in
 * postprocessing as to not get context object when
 * an exception will be thrown later in the process.
 *
 * @author nija123098
 * @since 1.0.0
 */
public enum ContextRequirement {
    USER(User.class, "This must have an invoker"),
    SHARD(Shard.class, "This must have a shard in the context"),
    CHANNEL(Channel.class, "You must use this in a channel"),
    GUILD(Guild.class, "You must use this in a guild"),
    MESSAGE(Message.class, "You must use this in response to a message"),
    REACTION(Reaction.class, "This command must be used with a reaction"),
    STRING(String.class, "This must have arguments"),;// both string and shard should always be provided
    private final String errorMessage;
    private Class<?> type;
    ContextRequirement(Class<?> type, String errorMessage) {
        this.type = type;
        this.errorMessage = errorMessage;
    }
    public String getErrorMessage() {
        return this.errorMessage;
    }
    public Class<?> getType() {
        return this.type;
    }
}
