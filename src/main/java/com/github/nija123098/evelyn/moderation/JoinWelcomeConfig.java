package com.github.nija123098.evelyn.moderation;

import com.github.nija123098.evelyn.command.CommandHandler;
import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordUserJoin;
import com.github.nija123098.evelyn.favor.FavorHandler;
import com.github.nija123098.evelyn.perms.BotRole;
import com.github.nija123098.evelyn.template.KeyPhrase;
import com.github.nija123098.evelyn.template.Template;
import com.github.nija123098.evelyn.template.TemplateHandler;

import java.util.Collections;

public class JoinWelcomeConfig extends AbstractConfig<Channel, Guild>{
    public JoinWelcomeConfig() {
        super("join_welcome", BotRole.GUILD_TRUSTEE, null, "If the bot should welcome a user when they join");
    }
    @EventListener
    public void handle(DiscordUserJoin leave){
        Channel channel = this.getValue(leave.getGuild());
        if (channel == null) return;// TODO WORKING ON check USER_JOIN and USER_LEAVE arguments
        Template template = TemplateHandler.getTemplate(KeyPhrase.USER_JOIN, leave.getGuild(), Collections.emptyList());
        if (template == null) return;
        GuildUser guildUser = GuildUser.getGuildUser(leave.getGuild(), leave.getUser());
        new MessageMaker(channel).appendRaw(template.interpret(leave.getUser(), null, null, leave.getGuild(), null, null, ConfigHandler.getSetting(GuildUserJoinTimeConfig.class, guildUser) == leave.getGuild().getJoinTimeForUser(leave.getUser()), FavorHandler.getFavorAmount(leave.getUser())));
    }
}
