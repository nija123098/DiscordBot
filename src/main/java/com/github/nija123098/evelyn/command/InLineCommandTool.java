package com.github.nija123098.evelyn.command;

import com.github.nija123098.evelyn.command.annotations.LaymanName;
import com.github.nija123098.evelyn.discordobjects.DiscordAdapter;
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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class InLineCommandTool {
    private static final long TIMEOUT = 300_000;
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
    private MessageMaker issueMaker = null;
    private boolean issue = false;
    public InLineCommandTool(ContextPack contextPack) {
        DiscordAdapter.increaseParserPoolSize();
        Map<User, InLineCommandTool> userMap = map.computeIfAbsent(contextPack.getChannel(), c -> new ConcurrentHashMap<>());
        if (userMap.containsKey(contextPack.getUser())) throw new ArgumentException("You can not be in two command sessions at once per server");
        userMap.put(contextPack.getUser(), this);
        this.thread = Thread.currentThread();
        this.contextPack = contextPack;
        this.messageMaker = new MessageMaker(this.contextPack.getUser(), this.contextPack.getMessage());
    }

    public void handle(Message userMessage) {
        String message = userMessage.getContent();
        if (message.equalsIgnoreCase("exit") || message.equalsIgnoreCase("e")) {
            if (issueMaker != null) issueMaker.sentMessage().delete();
            this.release();
            throw new CommandExitException();
        }
        if (checkDefaultRequest(message)) {
            this.usingDefault.set(true);
        } else {
            try {
                this.object.set(InvocationObjectGetter.convert(this.type.get(), this.contextPack.getUser(), this.contextPack.getShard(), this.contextPack.getChannel(), this.contextPack.getGuild(), this.contextPack.getMessage(), this.contextPack.getReaction(), message).getKey());
                this.didFind.set(true);
                if (issue) issueMaker.sentMessage().delete();
                userMessage.delete();
                this.thread.interrupt();
            } catch (UserIssueException e) {
                if (issueMaker != null) issueMaker.sentMessage().delete();
                issueMaker = e.makeMessage(this.contextPack.getChannel());
                userMessage.delete();
                issueMaker.append("\nPlease input a valid " + (this.type.get().isAnnotationPresent(LaymanName.class) ? this.type.get().getAnnotation(LaymanName.class).value() : this.type.get().getSimpleName()) + "\nType `exit` to exit the command process.").send();
                issue = true;
            }
        }
    }

    public <E> E requestValue(Class<E> e, MessageMaker commandMaker) {
        return this.requestValue(e, null, false, commandMaker);
    }

    public <E> E requestValue(Class<E> e, E defaultValue, MessageMaker commandMaker) {
        return this.requestValue(e, defaultValue, true, commandMaker);
    }

    private <E> E requestValue(Class<E> e, E defaultValue, boolean allowDefaultValue, MessageMaker commandMaker) {
        InvocationObjectGetter.checkConvertType(e);
        this.type.set(e);
        this.allowDefault.set(allowDefaultValue);
        this.usingDefault.set(false);
        this.messageMaker = commandMaker;
        this.messageMaker.send();
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
        return message.equals("d") || message.toLowerCase().equals("default");
    }

    public void release(){
        Message message = this.messageMaker.sentMessage();
        if (message != null) message.delete();
        if (map.get(this.contextPack.getChannel()).size() == 1) map.remove(this.contextPack.getChannel());
        DiscordAdapter.decreaseParserPoolSize();
    }

    public MessageMaker getMaker() {
        return this.messageMaker;
    }
}