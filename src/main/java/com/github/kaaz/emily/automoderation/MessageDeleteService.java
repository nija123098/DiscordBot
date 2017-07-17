package com.github.kaaz.emily.automoderation;

import com.github.kaaz.emily.discordobjects.wrappers.Message;
import com.github.kaaz.emily.service.AbstractService;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * Made by nija123098 on 5/10/2017.
 */
public class MessageDeleteService extends AbstractService {
    private static final List<Message> TO_DELETE = new CopyOnWriteArrayList<>();
    public static void delete(List<Message> messages){
        messages = messages.stream().filter(message -> !message.isPinned()).collect(Collectors.toList());
        if (messages.size() == 0) return;
        if (messages.size() > 100) messages.removeAll(messages.get(0).getChannel().bulkDelete(messages.subList(0, 100)));
        TO_DELETE.addAll(messages);
    }
    public MessageDeleteService() {
        super(500);
    }
    @Override
    public void run() {
        if (TO_DELETE.size() != 0) TO_DELETE.get(0).delete();
    }
}
