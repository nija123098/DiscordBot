package com.github.kaaz.discordbot.service;

/**
 * The general class to be extended for any service.
 *
 * A service runs multiple times in the uptime of
 * a program.  If such is not true then the code
 * to be run should not use the service framework.
 *
 * Services will not often do not block, but if they
 * do they must indicate so, so the service handler may
 * handle the service appropriately.  A check for blocking
 * is only made a single time, changing the return value
 * will do nothing after initalization for the ServiceHandler
 * class.
 *
 * @author nija123098
 * @since 2.0.0
 * @see ServiceHandler
 */
public abstract class AbstractService implements Runnable {
    private long delayBetween;

    /**
     * A standard constructor to assign
     * the delay between executions of run
     *
     * @param delayBetween the delay in millis between running
     */
    public AbstractService(long delayBetween) {
        this.delayBetween = delayBetween;
    }

    /**
     * A standard getter
     *
     * @return the delay between run calls,
     * including run invocation time
     */
    public long getDelayBetween(){
        return this.delayBetween;
    }

    /**
     * A getter called only at the initialization
     * of ServiceHandler to indicate if calling run
     * may block the thread.
     *
     * @return if calling run may block
     */
    public boolean mayBlock(){
        return false;
    }

    /**
     * To get if the service should run
     * after the set amount of time.
     * If {@code false} then the delay
     * will start once again.
     *
     * @return if the thread should run
     * the service
     */
    public boolean shouldRun(){
        return true;
    }
}
