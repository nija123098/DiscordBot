package com.github.nija123098.evelyn.information.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordGuildJoin;
import com.github.nija123098.evelyn.launcher.Launcher;
import com.github.nija123098.evelyn.moderation.logging.BotChannelConfig;

public class GuildIsNewConfig extends AbstractConfig<Boolean, Guild> {
    public GuildIsNewConfig() {
        super("guild_is_new", ConfigCategory.STAT_TRACKING, true, "If the guild had been served previously");
        Launcher.registerAsyncStartup(() -> DiscordClient.getGuilds().forEach(guild -> {
            if (!this.getValue(guild)) return;
            this.setValue(guild, false);
            welcome(guild);
        }));
    }
    @EventListener
    public void handle(DiscordGuildJoin join){
        this.setValue(join.getGuild(), false);
        welcome(join.getGuild());
    }
    private static void welcome(Guild guild){
        Channel channel = ConfigHandler.getSetting(BotChannelConfig.class, guild);
        MessageMaker maker = new MessageMaker(channel == null ? guild.getGeneralChannel() != null ? guild.getGeneralChannel() : guild.getChannels().get(0) : channel);
        maker.append("Thank you for adding me to this server!\nI always respond to being mentioned!  To add a prefix do @Evelyn prefix myPrefix.\nI also come with a `@Evelyn guide`\nFeel free to complain to my developers before you send me away, they are so desperate it's a joke.").send();
    }
}
