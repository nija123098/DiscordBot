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
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordGuildLeave;
import com.github.nija123098.evelyn.util.Time;

import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class GuildHistoryConfig extends AbstractConfig<Set<String>, Guild> {
    public GuildHistoryConfig() {
        super("guild_history", "", ConfigCategory.STAT_TRACKING, new HashSet<>(0), "Record of guilds leaving and joining");
    }

    @EventListener
    public void handle(DiscordGuildJoin join) {
        Guild guild = join.getGuild();
        MessageMaker maker = new MessageMaker(Channel.getChannel(ConfigProvider.BOT_SETTINGS.guildLogChannel())).withColor(new Color(39, 209, 110));
        this.alterSetting(guild, strings -> strings.add("joined: " + System.currentTimeMillis()));
        maker.getTitle().appendRaw(guild.getName());
        maker.withAuthor(guild.getOwner()).withAuthorIcon(guild.getOwner().getAvatarURL());
        maker.withThumb(guild.getIconURL().equals("https://cdn.discordapp.com/icons/214674233505087488/null.jpg") ? "https://cdn.discordapp.com/attachments/398634800384311300/419641007811067909/discord_white.png" : guild.getIconURL());
        maker.appendRaw("\u200b\nID: " + guild.getID() + "\nUsers: " + guild.getUsers().size());
        List<User> bots = guild.getUsers().stream().filter(User::isBot).collect(Collectors.toList());
        StringBuilder botList = new StringBuilder();
        for (User user : bots) {
            botList.append("```\n" + user.getNameAndDiscrim()).append("\nSince: ").append(Time.getAbbreviated(System.currentTimeMillis() - guild.getJoinTimeForUser(user))).append("\n```");
        }
        maker.getNewFieldPart().withBoth("Bots", botList.toString());
        maker.send();
    }

    @EventListener
    public void handle(DiscordGuildLeave leave) {
        Guild guild = leave.getGuild();
        String timeInGuild = Time.getAbbreviated(System.currentTimeMillis() - Long.parseLong(this.getValue(guild).iterator().next().substring(8)));
        this.alterSetting(guild, strings -> strings.add("left: " + System.currentTimeMillis()));
        MessageMaker maker = new MessageMaker(Channel.getChannel(ConfigProvider.BOT_SETTINGS.guildLogChannel())).withColor(new Color(255, 0 ,0));
        maker.getTitle().appendRaw(guild.getName());
        maker.withThumb(guild.getIconURL().equals("https://cdn.discordapp.com/icons/214674233505087488/null.jpg") ? "https://cdn.discordapp.com/attachments/398634800384311300/419641007811067909/discord_white.png" : guild.getIconURL());
        maker.withAuthor(guild.getOwner()).withAuthorIcon(guild.getOwner().getAvatarURL());
        maker.appendRaw("\u200b\nID: " + guild.getID() + "\nUsers: " + guild.getUsers().size() + "\nTime in guild: " + timeInGuild);
        List<User> bots = guild.getUsers().stream().filter(User::isBot).collect(Collectors.toList());
        StringBuilder botList = new StringBuilder();
        for (User user : bots) {
            botList.append("```\n" + user.getNameAndDiscrim()).append("\nSince: ").append(Time.getAbbreviated(System.currentTimeMillis() - guild.getJoinTimeForUser(user))).append("\n```");
        }
        maker.getNewFieldPart().withBoth("Bots", botList.toString());
        maker.send();
    }
}
