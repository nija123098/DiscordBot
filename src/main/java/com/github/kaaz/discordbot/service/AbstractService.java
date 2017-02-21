package com.github.kaaz.discordbot.service;

/**
 * Made by nija123098 on 2/20/2017.
 */
public abstract class AbstractService implements Runnable {
    private long delayBetween;
    public AbstractService(long delayBetween) {
        this.delayBetween = delayBetween;
    }
    public long getDelayBetween(){
        return this.delayBetween;
    }
    public boolean mayBlock(){
        return false;
    }
    public boolean shouldRun(){
        return true;
    }
}
