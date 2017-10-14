package com.github.nija123098.evelyn.moderation;

import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordPermission;
import com.github.nija123098.evelyn.discordobjects.wrappers.Message;
import com.github.nija123098.evelyn.exeption.PermissionsException;
import com.github.nija123098.evelyn.service.AbstractService;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * Made by nija123098 on 5/10/2017.
 */
public class MessageDeleteService extends AbstractService {
    private static final List<Message> TO_DELETE = new CopyOnWriteArrayList<>();
    public static void delete(List<Message> messages){
        if (messages.isEmpty()) return;
        PermissionsException.checkPermissions(messages.get(0).getChannel(), DiscordPermission.MANAGE_MESSAGES, DiscordPermission.READ_MESSAGES, DiscordPermission.READ_MESSAGE_HISTORY);
        messages = messages.stream().filter(message -> !message.isPinned()).collect(Collectors.toList());
        if (messages.isEmpty()) return;
        Channel channel = messages.get(0).getChannel();
        if (!channel.isPrivate()){
            List<Message> deleted;
            do {if (messages.size() < 3) break;
                deleted = channel.bulkDelete(messages.subList(0, Math.min(100, messages.size())));
                messages.removeAll(deleted);
            } while (deleted.size() == 100);
        }
        TO_DELETE.addAll(messages);
    }
    public MessageDeleteService() {
        super(500);
    }
    @Override
    public void run() {
        if (!TO_DELETE.isEmpty()) TO_DELETE.remove(0).delete();
    }
}
