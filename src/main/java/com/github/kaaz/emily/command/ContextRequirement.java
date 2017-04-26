package com.github.kaaz.emily.command;

/**
 * Made by nija123098 on 4/17/2017.
 */
public enum ContextRequirement {
    USER("This must have an invoker"),
    SHARD("This must have a shard in the context"),
    CHANNEL("You must use this in a channel"),
    GUILD("You must use this in a guild"),
    MESSAGE("You must use this in response to a message"),
    REACTION("This command must be used with a reaction"),
    STRING("This must have arguments"),;// both string and shard should always be provided
    private final String errorMessage;
    ContextRequirement(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    public String getErrorMessage() {
        return this.errorMessage;
    }
}
