package com.github.nija123098.evelyn.information.configs;

import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordGuildLeave;
import com.github.nija123098.evelyn.util.Time;
import sx.blah.discord.util.DiscordException;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class GuildLeaveLogConfig extends AbstractConfig<Long, Guild> {
    public GuildLeaveLogConfig() {
        super("guild_leave_log", "", ConfigCategory.STAT_TRACKING, new Long(0), "Record of guilds leaving and joining");
    }

    @EventListener
    public void handle(DiscordGuildLeave leave) {
        Guild guild = leave.getGuild();
        User owner = guild.getOwner();
        long previous = guild.getJoinTimeForUser(DiscordClient.getOurUser());
        String timeInGuild = Time.getAbbreviated(System.currentTimeMillis() - previous);
        this.setValue(guild, System.currentTimeMillis());
        MessageMaker maker = new MessageMaker(Channel.getChannel(ConfigProvider.BOT_SETTINGS.guildLogChannel()));
        maker.withAuthorIcon(ConfigProvider.URLS.redArrowPng()).withColor(new Color(255, 0 ,0));
        maker.appendRaw("\u200b                                           \u200b\nDate created: " + Time.getDate(guild.getCreationDate()) + "\nUsers: " + (guild.getUsers().stream().filter(user -> !user.isBot()).collect(Collectors.toList()).size()) + "\nTime in guild: " + timeInGuild + "\nTotal guilds: " + DiscordClient.getGuilds().size());
        GuildJoinLogConfig.formatMessage(guild, owner, maker);
    }
}
