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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class GuildJoinLogConfig extends AbstractConfig<Set<String>, Guild> {
    public GuildJoinLogConfig() {
        super("guild_join_log", "", ConfigCategory.STAT_TRACKING, new HashSet<>(0), "Record of guilds leaving and joining");
    }

    @EventListener
    public void handle(DiscordGuildJoin join) {
        Guild guild = join.getGuild();
        User owner = guild.getOwner();
        MessageMaker maker = new MessageMaker(Channel.getChannel(ConfigProvider.BOT_SETTINGS.guildLogChannel())).withColor(new Color(39, 209, 110));
        this.alterSetting(guild, strings -> strings.add("joined: " + System.currentTimeMillis()));
        maker.getTitle().appendRaw(guild.getName());
        maker.withAuthor(owner).withAuthorIcon(ConfigProvider.URLS.greenArrowPng());
        maker.withThumb(guild.getIconURL().contains("null") ? ConfigProvider.URLS.discordWhitePng() : guild.getIconURL());
        maker.appendRaw("\u200b                                           \u200b\nID: " + guild.getID() + "\nAge: " + Time.getAbbreviated(System.currentTimeMillis() - guild.getCreationDate()) + "\nUsers: " + guild.getUsers().stream().filter(user -> !user.isBot()).collect(Collectors.toList()).size() + "\nTotal guilds: " + DiscordClient.getGuilds().size());
        maker.withTimestamp(System.currentTimeMillis());
        List<User> bots = guild.getUsers().stream().filter(user -> user.isBot() && !user.equals(DiscordClient.getOurUser())).collect(Collectors.toList());
        if (bots.size() > 0) {
            StringBuilder botList = new StringBuilder();
            for (User user : bots) {
                try {
                    botList.append("```\n" + user.getNameAndDiscrim()).append("\nSince: ").append(Time.getAbbreviated(System.currentTimeMillis() - guild.getJoinTimeForUser(user))).append("\n```");
                } catch (DiscordException ignored) {}
            }
            if (botList.toString().length() > 1000) {
                maker.getNewFieldPart().withBoth("Bots", "" + bots.size());
            } else {
                maker.getNewFieldPart().withBoth("Bots: " + bots.size(), botList.toString());
            }
        }
        maker.send();
    }
}
