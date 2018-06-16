package com.github.nija123098.evelyn.command;

import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Message;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventDistributor;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordMessageReceived;
import com.github.nija123098.evelyn.exception.ArgumentException;
import com.github.nija123098.evelyn.exception.CommandExitException;
import com.github.nija123098.evelyn.exception.DevelopmentException;
import com.github.nija123098.evelyn.exception.UserIssueException;
import com.github.nija123098.evelyn.util.CareLess;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class InLineCommandTool {
    private static final long TIMEOUT = 180_000;
    private static final Map<Channel, Map<User, InLineCommandTool>> map = new ConcurrentHashMap<>();
    static {
        EventDistributor.register(InLineCommandTool.class);
    }
    @EventListener
    public static void handler(DiscordMessageReceived event) {
        Map<User, InLineCommandTool> userMap = map.get(event.getChannel());
        if (userMap == null) return;
        InLineCommandTool tool = userMap.get(event.getAuthor());
        if (tool != null) tool.handle(event.getMessage());
    }
    private Thread thread;
    private ContextPack contextPack;
    private final AtomicReference<Class<?>> type = new AtomicReference<>();
    private final AtomicReference<Object> object = new AtomicReference<>();
    private final AtomicBoolean didFind = new AtomicBoolean(), allowDefault = new AtomicBoolean(), usingDefault = new AtomicBoolean();
    private MessageMaker messageMaker;
    public InLineCommandTool(ContextPack contextPack) {
        Map<User, InLineCommandTool> userMap = map.computeIfAbsent(contextPack.getChannel(), c -> new ConcurrentHashMap<>());
        if (userMap.containsKey(contextPack.getUser())) throw new ArgumentException("You can not be in two command sessions at once per server");
        userMap.put(contextPack.getUser(), this);
        this.thread = Thread.currentThread();
        this.contextPack = contextPack;
        this.messageMaker = new MessageMaker(this.contextPack.getUser(), this.contextPack.getMessage());
    }

    public void handle(Message userMessage) {
        String content = userMessage.getContent();
        if (content.equalsIgnoreCase("exit") || content.equalsIgnoreCase("e")) {
            this.release();
            throw new CommandExitException();
        }
        if (checkDefaultRequest(content)) {
            this.usingDefault.set(true);
            this.thread.interrupt();
        } else {
            try {
                this.object.set(InvocationObjectGetter.convert(this.type.get(), this.contextPack.getUser(), this.contextPack.getShard(), this.contextPack.getChannel(), this.contextPack.getGuild(), this.contextPack.getMessage(), this.contextPack.getReaction(), content).getKey());
                this.didFind.set(true);
                userMessage.delete();
                this.thread.interrupt();
            } catch (UserIssueException e) {
                userMessage.delete();
                e.makeMessage(this.contextPack.getChannel()).setMessage(this.messageMaker.sentMessage()).send();
                CareLess.lessSleep(5_000);
                this.messageMaker.forceCompile().send();
            }
        }
    }

    public <E> E requestValue(Class<E> e, String s) {
        return this.requestValue(e, null, false, s);
    }

    public <E> E requestValue(Class<E> e, E defaultValue, String s) {
        return this.requestValue(e, defaultValue, true, s);
    }

    private <E> E requestValue(Class<E> e, E defaultValue, boolean allowDefaultValue, String text) {
        InvocationObjectGetter.checkConvertType(e);
        this.messageMaker.forceCompile().getHeader().clear().append(text).getMaker().send();
        this.type.set(e);
        this.allowDefault.set(allowDefaultValue);
        this.usingDefault.set(false);
        try {
            Thread.sleep(TIMEOUT);
            throw new ArgumentException("Timeout");
        } catch (InterruptedException ex) {
            if (this.didFind.getAndSet(false)) return this.usingDefault.get() ? defaultValue : (E) this.object.getAndSet(null);
            this.release();
            throw new DevelopmentException("Unexpected interruption while requesting inline value", ex);
        }
    }

    private boolean checkDefaultRequest(String message){
        return message.equalsIgnoreCase("d") || message.equalsIgnoreCase("default");
    }

    public void release(){
        Message message = this.messageMaker.sentMessage();
        if (message != null) message.delete();
        if (map.get(this.contextPack.getChannel()).size() == 1) map.remove(this.contextPack.getChannel());
    }

    public MessageMaker getMaker() {
        return this.messageMaker;
    }
}