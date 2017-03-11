package com.github.kaaz.discordbot.discordobjects.exception;

import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RequestBuffer;

/**
 * Made by nija123098 on 3/8/2017.
 */
public class WrapperHelper {
    public static <E> E wrap(Request<E> request) {
        return innerWrap(request);
    }
    public static void wrap(VoidRequest request) {
        innerWrap(request);
    }
    private static <E> E innerWrap(Request<E> request) {
        try {
            return RequestBuffer.request((RequestBuffer.IRequest<E>) request::request).get();
        } catch (RuntimeException e) {
            if (e instanceof MissingPermissionsException) {
                throw new MissingPermException((MissingPermissionsException) e);
            }
            if (e instanceof DiscordException) {
                throw new DException((DiscordException) e);
            }
            throw e;
        }
    }
    @FunctionalInterface
    public interface Request<T> {
        T request();
    }
    @FunctionalInterface
    public interface VoidRequest extends Request<Void>{
        default Void request(){
            doRequest();
            return null;
        }
        void doRequest();
    }
}
