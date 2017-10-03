package com.github.nija123098.evelyn.discordobjects.exception;

import sx.blah.discord.util.DiscordException;

/**
 * A wrapper exception for Discord related exceptions.
 *
 * @author nija123098
 * @since 1.0.0
 */
public class DException extends RuntimeException {
    DException(DiscordException e){
        super(e);
    }
}
