package com.github.nija123098.evelyn.discordobjects;

import com.github.nija123098.evelyn.exeption.DException;
import com.github.nija123098.evelyn.exeption.PermissionsException;
import com.github.nija123098.evelyn.util.Care;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;
import sx.blah.discord.util.RequestBuffer;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * An class to assist in error wrapping and rate limiting.
 *
 * All Discord API methods that can throw errors should be
 * wrapped with this class' methods in order to wrap any
 * errors that those methods throw as well as handle rate
 * limiting.  In the case of rate limiting the methods will
 * block until the API throws an error or the API request
 * is completed.  This is to ensure that at the end of the
 * method call the action is completed so that a request
 * will not fail without the bot being notified programmatically.
 *
 * @author nija123098
 * @since 1.0.0
 * @see PermissionsException
 * @see DException
 */
public class ErrorWrapper {
    public static <E> E wrap(Request<E> request) {
        return innerWrap(request);
    }
    public static void wrap(VoidRequest request) {
        innerWrap(request);
    }

    /**
     * Executes a {@link Request} and blocks until it completes
     * or throws wrapped exceptions the request caused.
     *
     * @param request the request to wrap and block for.
     * @param <E> the type to return.
     * @return the value returned by the request.
     */
    private static <E> E innerWrap(Request<E> request) {
        AtomicBoolean complete = new AtomicBoolean();
        AtomicReference<E> objectReference = new AtomicReference<>();
        AtomicReference<RuntimeException> exceptionReference = new AtomicReference<>();
        RequestBuffer.request(() -> {
            try {
                objectReference.set(request.request());
            } catch (RuntimeException e){
                if (e instanceof RateLimitException) throw e;
                exceptionReference.set(e);
            }
            complete.set(true);
        }).get();
        try {
            while (true) {
                if (!complete.get()) continue;
                if (exceptionReference.get() == null) return objectReference.get();
                throw exceptionReference.get();
            }
        } catch (MissingPermissionsException e){// return RequestBuffer.request((RequestBuffer.IRequest<E>) request::request).get();
            throw new PermissionsException(e);
        } catch (DiscordException e){
            if (e.getMessage().contains("cloudflare-nginx")) {
                Care.lessSleep(250);
                return innerWrap(request);
            }
            throw new DException(e);
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
