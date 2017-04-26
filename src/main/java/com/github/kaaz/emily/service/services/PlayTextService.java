package com.github.kaaz.emily.service.services;

import com.github.kaaz.emily.discordobjects.wrappers.DiscordClient;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.service.AbstractService;
import com.github.kaaz.emily.template.KeyPhrase;
import com.github.kaaz.emily.template.Template;
import com.github.kaaz.emily.template.TemplateHandler;

import java.util.List;

/**
 * Made by nija123098 on 4/24/2017.
 */
public class PlayTextService extends AbstractService {
    private static final long PLAY_TEXT_SPEED = 60_000;
    private static final List<Template> PREVIOUS = new MemoryManagementService.ManagedList<>(PLAY_TEXT_SPEED + 1000);// a second for execution time
    public PlayTextService() {
        super(PLAY_TEXT_SPEED);
    }
    @Override
    public void run() {
        Template template = TemplateHandler.getTemplate(KeyPhrase.PLAY_TEXT, null, PREVIOUS);
        if (template != null) DiscordClient.getShards().forEach(shard -> shard.online(template.interpret((User) null, shard, null, null, null, null)));
    }
}
