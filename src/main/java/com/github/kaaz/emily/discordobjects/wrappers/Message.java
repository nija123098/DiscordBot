package com.github.kaaz.emily.discordobjects.wrappers;

import sx.blah.discord.handle.obj.IMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Made by nija123098 on 3/4/2017.
 */
public class Message {// should not be kept stored, too many are made
    static Message getMessage(IMessage iMessage){
        return null;
    }
    static Message getMessage(String id){
        return null;
    }
    static List<Message> getMessages(List<IMessage> iMessages){
        List<Message> messages = new ArrayList<>(iMessages.size());
        iMessages.forEach(iMessage -> messages.add(getMessage(iMessage)));
        return messages;
    }
    static List<IMessage> getIMessages(List<Message> messages){
        List<IMessage> iMessages = new ArrayList<>(messages.size());
        messages.forEach(message -> iMessages.add(message.message()));
        return iMessages;
    }
    private final IMessage iMessage;
    private Message(IMessage message){
        iMessage = message;
    }
    IMessage message() {
        return iMessage;
    }
}