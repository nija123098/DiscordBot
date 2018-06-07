package com.github.nija123098.evelyn.information.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordGuildJoin;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordGuildLeave;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordMessageSend;
import com.github.nija123098.evelyn.moderation.logging.BotChannelConfig;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class GuildIsNewConfig extends AbstractConfig<Boolean, Guild> {
    public GuildIsNewConfig() {
        super("guild_is_new", "", ConfigCategory.STAT_TRACKING, true, "If the guild had been served previously");
    }
    @EventListener
    public void handle(DiscordMessageSend send) {
        if (send.getChannel().isPrivate()) return;
        welcome(send.getGuild());
    }
    @EventListener
    public void handle(DiscordGuildJoin join) {
        welcome(join.getGuild());
    }
    @EventListener
    public void handle(DiscordGuildLeave leave) {
        this.reset(leave.getGuild());
    }
    private void welcome(Guild guild) {
        if (!this.getValue(guild)) return;
        this.setValue(guild, false);
        Channel channel = ConfigHandler.getSetting(BotChannelConfig.class, guild);
        channel = channel == null ? guild.getGeneralChannel() != null ? guild.getGeneralChannel() : guild.getChannels().stream().filter(Channel::canPost).findFirst().orElse(null) : channel;
        if (channel != null) new MessageMaker(channel).append("Thank you for adding me to this server!\nI always respond to being mentioned!  To change the default `!` prefix do @Evelyn prefix `new prefix`.\nI have a `@Evelyn setup` command which you can use to setup a bot config channel and log channels automatically.\nI also come with a `@Evelyn guide`\nUse `@Evelyn changelog` to see the latest changes!").send();
    }
}
