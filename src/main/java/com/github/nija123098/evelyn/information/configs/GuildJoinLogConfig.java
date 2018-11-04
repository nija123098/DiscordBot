package com.github.nija123098.evelyn.information.configs;

import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordGuildJoin;
import com.github.nija123098.evelyn.moderation.logging.Logging;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class GuildJoinLogConfig extends AbstractConfig<Long, Guild> {
    public GuildJoinLogConfig() {
        super("guild_join_log", "", ConfigCategory.STAT_TRACKING, new Long(0), "Record of guilds leaving and joining");
    }

    @EventListener
    public void handle(DiscordGuildJoin join) {
        Guild guild = join.getGuild();
        User owner = guild.getOwner();
        MessageMaker maker = new MessageMaker(Channel.getChannel(ConfigProvider.BOT_SETTINGS.guildLogChannel()));
        this.setValue(guild, System.currentTimeMillis());
        Logging.GUILD_JOIN.botGuildLog(maker, guild, owner);
    }
}
