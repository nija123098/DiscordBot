package com.github.kaaz.emily.util;

import com.github.kaaz.emily.discordobjects.wrappers.Channel;
import com.github.kaaz.emily.discordobjects.wrappers.DiscordClient;
import com.github.kaaz.emily.discordobjects.wrappers.event.EventDistributor;
import com.github.kaaz.emily.discordobjects.wrappers.event.EventListener;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordMessageSend;
import com.github.kaaz.emily.launcher.BotConfig;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.MessageBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Made by nija123098 on 6/4/2017.
 */
public class ImageAttacher {
    private static final Map<String, String> MAP = new ConcurrentHashMap<>();
    private static final IDiscordClient CLIENT = DiscordClient.getClientForShard(Channel.getChannel(BotConfig.IMAGE_LOAD_CHANNEL + "").getShard());
    static {
        EventDistributor.register(ImageAttacher.class);
    }
    public static String getUrl(File file){
        String ret = MAP.get(file.getPath());
        if (ret == null) {
            try{new MessageBuilder(CLIENT).withChannel(BotConfig.IMAGE_LOAD_CHANNEL).withContent(file.getPath()).withFile(file).send();
            }catch(FileNotFoundException e){Log.log("Exception not uploading file for url use", e);}
        }
        Care.lessSleep(100);
        while ((ret = MAP.remove(file.getPath())) == null){
            Care.lessSleep(50);
        }
        return ret;
    }
    @EventListener
    public static void handle(DiscordMessageSend event){
        if (event.getChannel().channel().getLongID() == BotConfig.IMAGE_LOAD_CHANNEL) MAP.put(event.getMessage().getContent(), event.getMessage().getAttachments().get(0).getUrl());
    }
}
