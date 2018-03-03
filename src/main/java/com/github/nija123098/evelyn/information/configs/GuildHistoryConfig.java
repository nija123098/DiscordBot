package com.github.nija123098.evelyn.information.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordGuildJoin;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordGuildLeave;

import java.util.HashSet;
import java.util.Set;

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
        MessageMaker maker = new MessageMaker(Channel.getChannel("419603233477099531"));
        this.alterSetting(guild, strings -> strings.add("joined: " + System.currentTimeMillis()));
        maker.getTitle().appendRaw("Guild Acquired");
        maker.withImage(guild.getIconURL());
        maker.getNewFieldPart().withBoth(guild.getName(), "ID: " + guild.getID() + "\nOwner: " + guild.getOwner().getNameAndDiscrim() + " ID: " + guild.getOwnerID() + "\nUsers: " + guild.getUsers().size());
        maker.send();
    }

    @EventListener
    public void handle(DiscordGuildLeave leave) {
        Guild guild = leave.getGuild();
        this.alterSetting(guild, strings -> strings.add("left: " + System.currentTimeMillis()));
        MessageMaker maker = new MessageMaker(Channel.getChannel("419603233477099531"));
        maker.getTitle().appendRaw("Guild Lost");
        maker.withImage(guild.getIconURL());
        maker.getNewFieldPart().withBoth(guild.getName(), "ID: " + guild.getID() + "\nOwner: " + guild.getOwner().getNameAndDiscrim() + " ID: " + guild.getOwnerID() + "\nUsers: " + guild.getUsers().size());
        maker.send();
    }
}
