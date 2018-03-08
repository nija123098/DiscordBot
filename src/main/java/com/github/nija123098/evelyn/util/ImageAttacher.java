package com.github.nija123098.evelyn.util;

import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordMessageSend;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.MessageBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.github.nija123098.evelyn.discordobjects.wrappers.Channel.getChannel;
import static com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient.getClientForShard;
import static com.github.nija123098.evelyn.discordobjects.wrappers.event.EventDistributor.register;
import static com.github.nija123098.evelyn.util.CareLess.lessSleep;
import static com.github.nija123098.evelyn.util.Log.log;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class ImageAttacher {
    private static final Map<String, String> MAP = new ConcurrentHashMap<>();
    private static final IDiscordClient CLIENT = getClientForShard(getChannel(0L + "").getShard());

    static {
        register(ImageAttacher.class);
    }

    public static String getUrl(File file) {
        String ret = MAP.get(file.getPath());
        if (ret == null) {
            try {
                new MessageBuilder(CLIENT).withChannel(0L).withContent(file.getPath()).withFile(file).send();
            } catch (FileNotFoundException e) {
                log("Exception not uploading file for url use", e);
            }
        }
        lessSleep(100);
        while ((ret = MAP.remove(file.getPath())) == null) {
            lessSleep(50);
        }
        return ret;
    }

    @EventListener
    public static void handle(DiscordMessageSend event) {
        if (event.getChannel().channel().getLongID() == 0L)
            MAP.put(event.getMessage().getContent(), event.getMessage().getAttachments().get(0).getUrl());
    }
}
