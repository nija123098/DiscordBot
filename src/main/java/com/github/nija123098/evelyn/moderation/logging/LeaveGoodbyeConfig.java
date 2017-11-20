package com.github.nija123098.evelyn.moderation.logging;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordUserLeave;
import com.github.nija123098.evelyn.template.KeyPhrase;
import com.github.nija123098.evelyn.template.Template;
import com.github.nija123098.evelyn.template.TemplateHandler;

import java.util.Collections;

public class LeaveGoodbyeConfig extends AbstractConfig<Channel, Guild> {
    public LeaveGoodbyeConfig() {
        super("leave_goodbye", "", ConfigCategory.LOGGING, (Channel) null, "If the bot should wish farewell to a user when they leave");
    }
    @EventListener
    public void handle(DiscordUserLeave leave){
        Channel channel = this.getValue(leave.getGuild());
        if (channel == null) return;
        Template template = TemplateHandler.getTemplate(KeyPhrase.USER_LEAVE, leave.getGuild(), Collections.emptyList());
        if (template == null) return;
        new MessageMaker(channel).appendRaw(template.interpret(leave.getUser(), null, null, leave.getGuild(), null, null, leave.responsibleUser(), leave.wasKicked(), leave.wasBanned())).send();
    }
}
