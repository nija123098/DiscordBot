package com.github.kaaz.emily.audio.configs;

import com.github.kaaz.emily.audio.commands.JoinCommand;
import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.discordobjects.wrappers.event.EventListener;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordVoiceJoin;
import com.github.kaaz.emily.perms.BotRole;

public class JoinMeConfig extends AbstractConfig<Boolean, User> {
    private static JoinMeConfig config;
    public JoinMeConfig() {
        super("join_me", BotRole.USER, false, "Makes the bot join you if it is available");
        config = this;
    }
    @EventListener
    public void handle(DiscordVoiceJoin join, MessageMaker maker){
        if (!config.getValue(join.getUser())) return;
        GuildAudioManager manager = GuildAudioManager.getManager(join.getGuild());
        if (join.getGuild().getConnectedVoiceChannel() == null) JoinCommand.command(manager, join.getChannel(), maker);
    }
}
