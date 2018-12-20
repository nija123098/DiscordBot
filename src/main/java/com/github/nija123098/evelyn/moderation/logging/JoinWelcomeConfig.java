package com.github.nija123098.evelyn.moderation.logging;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordUserJoin;
import com.github.nija123098.evelyn.favor.FavorHandler;
import com.github.nija123098.evelyn.moderation.GuildUserJoinTimeConfig;
import com.github.nija123098.evelyn.template.KeyPhrase;
import com.github.nija123098.evelyn.template.Template;
import com.github.nija123098.evelyn.template.TemplateHandler;

import java.util.Collections;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class JoinWelcomeConfig extends AbstractConfig<Channel, Guild>{
    public JoinWelcomeConfig() {
        super("join_welcome", "Welcome Message", ConfigCategory.LOGGING, (Channel) null, "If the bot should welcome a user when they join");
    }
    @EventListener
    public void handle(DiscordUserJoin join) {
        Channel channel = this.getValue(join.getGuild());
        if (channel == null) return;
        Template template = TemplateHandler.getTemplate(KeyPhrase.USER_JOIN, join.getGuild(), Collections.emptyList());
        if (template == null) return;
        GuildUser guildUser = GuildUser.getGuildUser(join.getGuild(), join.getUser());
        new MessageMaker(channel).appendRaw(template.interpret(join.getUser(), channel.getShard(), null, join.getGuild(), null, null, ConfigHandler.getSetting(GuildUserJoinTimeConfig.class, guildUser) == guildUser.getJoinTime(), FavorHandler.getFavorAmount(join.getUser()))).send();
    }
}
