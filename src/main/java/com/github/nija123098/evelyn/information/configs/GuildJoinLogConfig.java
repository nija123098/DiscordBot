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
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordGuildJoin;
import com.github.nija123098.evelyn.util.Time;
import sx.blah.discord.util.DiscordException;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

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
        maker.getHeader().appendRaw("Owner: " + owner.getNameAndDiscrim() + " | " + owner.getID());
        maker.getAuthorName().appendRaw(guild.getName());
        maker.withThumb(guild.getIconURL().contains("null") ? ConfigProvider.URLS.discordWhitePng() : guild.getIconURL());
        maker.withAuthorIcon(ConfigProvider.URLS.greenArrowPng()).withColor(new Color(39, 209, 110));
        maker.appendRaw("\u200b                                           \u200b\nDate created: " + Time.getDate(guild.getCreationDate()) + "\nUsers: " + (guild.getUsers().stream().filter(user -> !user.isBot()).collect(Collectors.toList()).size()) + "\nTotal guilds: " + DiscordClient.getGuilds().size());
        maker.getNewFieldPart().withInline(true).withBoth("Channels", String.valueOf(guild.getChannels().size()));
        maker.getNewFieldPart().withInline(true).withBoth("Categories", String.valueOf(guild.getCategories().size()));
        maker.getNewFieldPart().withInline(true).withBoth("Roles", String.valueOf(guild.getRoles().size()));
        maker.getNewFieldPart().withInline(true).withBoth("Region", guild.getRegion().getName());
        maker.withTimestamp(System.currentTimeMillis());
        List<User> bots = guild.getUsers().stream().filter(User::isBot).collect(Collectors.toList());
        if (bots.size() > 0) {
            StringBuilder botList = new StringBuilder();
            for (User user : bots) {
                try {
                    if (!user.equals(DiscordClient.getOurUser())) {
                        botList.append("```\n" + user.getNameAndDiscrim()).append("\nSince: ").append(Time.getDate(guild.getJoinTimeForUser(user))).append("\n```");
                    }
                } catch (DiscordException ignored) {}
            }
            if (botList.toString().length() > 1000) {
                maker.getNewFieldPart().withBoth("Bots", "" + bots.size());
            } else {
                maker.getNewFieldPart().withBoth("Bots: " + (bots.size() - 1), botList.toString());
            }
        }
        maker.send();
    }
}