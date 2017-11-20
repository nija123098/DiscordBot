package com.github.nija123098.evelyn.audio.configs;

import com.github.nija123098.evelyn.audio.commands.JoinCommand;
import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordVoiceJoin;

public class JoinMeConfig extends AbstractConfig<Boolean, User> {
    private static JoinMeConfig config;
    public JoinMeConfig() {
        super("join_me", "", ConfigCategory.GUILD_PERSONALIZATION, false, "Makes the bot join you if it is available");
        config = this;
    }
    @EventListener
    public void handle(DiscordVoiceJoin join, MessageMaker maker){
        if (!config.getValue(join.getUser())) return;
        GuildAudioManager manager = GuildAudioManager.getManager(join.getGuild());
        if (join.getGuild().getConnectedVoiceChannel() == null) JoinCommand.command(manager, join.getChannel(), maker);
    }
}
