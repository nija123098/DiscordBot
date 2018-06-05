package com.github.nija123098.evelyn.discordobjects;

import com.github.nija123098.evelyn.exception.DException;
import com.github.nija123098.evelyn.exception.PermissionsException;
import com.github.nija123098.evelyn.util.CareLess;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;
import sx.blah.discord.util.RequestBuffer;

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
public class ExceptionWrapper {
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
        try {
            return request.request();// initial attempt
        } catch (MissingPermissionsException p) {
            throw new PermissionsException(p);
        } catch (Exception ex) {
            AtomicReference<E> objectReference = new AtomicReference<>();
            AtomicReference<RuntimeException> exceptionReference = new AtomicReference<>();
            CareLess.lessSleep(100);
            RequestBuffer.request(() -> {// initial attempt failed likely due to rate limiting, retrying.
                try {
                    objectReference.set(request.request());
                } catch (RuntimeException e) {
                    if (e instanceof RateLimitException) throw e;
                    exceptionReference.set(e);
                }
                synchronized (request) {
                    request.notify();
                }
            }).get();
            try {
                synchronized (request) {
                    Thread.currentThread().wait();
                }
            } catch (InterruptedException | IllegalMonitorStateException ignored) {}// took long enough
            if (exceptionReference.get() == null) return objectReference.get();
            if (MissingPermissionsException.class.isAssignableFrom(exceptionReference.get().getClass())) {// handle cases where wrapping is necessary
                throw new PermissionsException((MissingPermissionsException) exceptionReference.get());
            } else if (DiscordException.class.isAssignableFrom(exceptionReference.get().getClass())) {
                if (exceptionReference.get().getMessage().contains("cloudflare-nginx")) {
                    CareLess.lessSleep(250);
                    return innerWrap(request);
                }
                throw new DException((DiscordException) exceptionReference.get());
            }
            throw exceptionReference.get();
        }
    }
    @FunctionalInterface
    public interface Request<T> {
        T request();
    }
    @FunctionalInterface
    public interface VoidRequest extends Request<Void>{
        default Void request() {
            doRequest();
            return null;
        }
        void doRequest();
    }
}
