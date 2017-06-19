package com.github.kaaz.emily.audio.commands;

import com.github.kaaz.emily.audio.configs.guild.SkipPercentConfig;
import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.discordobjects.wrappers.VoiceChannel;
import com.github.kaaz.emily.discordobjects.wrappers.event.EventListener;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordTrackEnd;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Made by nija123098 on 5/9/2017.
 */
public class SkipCommand extends AbstractCommand {
    private static final Map<GuildAudioManager, Set<User>> MAP = new ConcurrentHashMap<>();
    public SkipCommand() {
        super("skip", ModuleLevel.MUSIC, null, null, "Skips the currently playing track");
    }
    @Command
    public void command(GuildAudioManager manager, User user, VoiceChannel channel, MessageMaker maker){
        MAP.compute(manager, (m, users) -> {
            if (users == null) users = new HashSet<>();
            users.add(user);
            return users;
        });
        float percent = MAP.get(manager).size() / (float) channel.getConnectedUsers().stream().filter(User::isBot).filter(u -> u.isDeaf(channel.getGuild())).count() * 100;
        int required = ConfigHandler.getSetting(SkipPercentConfig.class, channel.getGuild());
        if (percent > required) manager.skipTrack();
        else maker.append((percent - required) + "% is required to skip this track");
    }
    @EventListener
    public void handle(DiscordTrackEnd end){
        GuildAudioManager manager = GuildAudioManager.getManager(end.getGuild());
        if (manager != null) MAP.remove(manager);
    }
}
