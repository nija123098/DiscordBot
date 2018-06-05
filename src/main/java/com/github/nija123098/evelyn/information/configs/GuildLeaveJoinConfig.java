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
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordGuildLeave;
import com.github.nija123098.evelyn.launcher.Launcher;
import com.github.nija123098.evelyn.util.CareLess;
import com.github.nija123098.evelyn.util.Time;
import javafx.util.Pair;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class GuildLeaveJoinConfig extends AbstractConfig<List<Pair<Long, Long>>, Guild> {
    private static final long NOT_OCCURRED = 0, UNKNOWN = -1L, GREATER_THAN_BOTH = 1;
    public GuildLeaveJoinConfig() {
        super("guild_leave_join", "", ConfigCategory.STAT_TRACKING, new ArrayList<>(1), "The history of a guild leaving and joining");
    }
    @EventListener
    public void handle(DiscordGuildJoin join) {
        if (!Launcher.isReady()) return;
        this.alterSetting(join.getGuild(), pairs -> {
            if (!pairs.isEmpty()) {
                Pair<Long, Long> pair = pairs.get(pairs.size() - 1);
                if (pair.getValue() < GREATER_THAN_BOTH) {
                    pairs.add(new Pair<>(pairs.remove(pairs.size() - 1).getKey(), UNKNOWN));
                }
            }
            pairs.add(new Pair<>(System.currentTimeMillis(), NOT_OCCURRED));
        });
    }
    @EventListener
    public void handle(DiscordGuildLeave leave) {
        this.alterSetting(leave.getGuild(), pairs -> {
            if (!pairs.isEmpty()) {
                Pair<Long, Long> pair = pairs.get(pairs.size() - 1);
                if (pair.getValue() == NOT_OCCURRED) pairs.add(new Pair<>(pairs.remove(pairs.size() - 1).getKey(), System.currentTimeMillis()));
            }
            pairs.add(new Pair<>(UNKNOWN, System.currentTimeMillis()));
        });
    }
    public void log(Guild guild, boolean join) {
        MessageMaker maker = new MessageMaker(Channel.getChannel(ConfigProvider.BOT_SETTINGS.guildLogChannel())).withColor(join ? Color.GREEN : Color.RED);
        maker.getTitle().appendRaw(guild.getName());
        maker.withThumb(guild.getIconURL().contains("null") ? ConfigProvider.URLS.discordWhitePng() : guild.getIconURL());
        maker.withTimestamp(System.currentTimeMillis());
        maker.withAuthor(guild.getOwner()).withAuthorIcon(join ? ConfigProvider.URLS.greenArrowPng() : ConfigProvider.URLS.redArrowPng());
        maker.getNote().appendRaw(guild.getID());
        maker.appendRaw("Total Guilds: " + DiscordClient.getGuilds().size());
        CareLess.something(() -> maker.getNewFieldPart().withBoth("Users", String.valueOf(guild.getUsers().size())));// behavior here is not guaranteed
        maker.appendRaw("\nGuild Age: " + Time.getAbbreviated(System.currentTimeMillis() - guild.getCreationDate()));
        if (!join) CareLess.something(() -> maker.appendRaw("\nTime in Guild: " + Time.getAbbreviated(System.currentTimeMillis() - guild.getJoinTimeForUser(DiscordClient.getOurUser()))));
        List<User> bots = guild.getUsers().stream().filter(User::isBot).collect(Collectors.toList());
        if (!bots.isEmpty()) {
            if (bots.size() > 10) maker.appendRaw("\nBots: " + bots.size());
            else {
                StringBuilder botLists = new StringBuilder(500);
                for (User user : bots) {
                    botLists.append("```\n").append(user.getNameAndDiscrim()).append("\nSince: ").append(Time.getAbbreviated(System.currentTimeMillis() - guild.getJoinTimeForUser(user))).append("\n```");
                }
                botLists.append(botLists);
            }
        }
        maker.send();
    }
}
