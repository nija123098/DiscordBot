package com.github.nija123098.evelyn.helping.send;

import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.command.annotations.Context;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

/**
 * @author Dxeo
 * @since 1.0.0
 */
public class SendFeedbackCommand extends AbstractCommand {
    public SendFeedbackCommand() {
        super(SendCommand.class, "feedback", null, null, "fb", "Send feedback to the bot devs");
    }

    @Command
    public void feedback(MessageMaker maker, Channel channel, @Context(softFail = true) Guild guild, User user, @Argument String msg) {

        maker.withChannel(Channel.getChannel(ConfigProvider.BOT_SETTINGS.feedbackChannel()));
        maker.withThumb(ConfigProvider.URLS.feedbackThumb());
        maker.getHeader().clear().append(msg);
        maker.withAuthor(user);
        maker.withAuthorIcon(user.getAvatarURL());
        maker.withTimestamp(System.currentTimeMillis());
        if (guild != null) {
            maker.getNote().clear().append(guild.getName() + " | " + guild.getID());
        } else {
            maker.getNote().clear().append("DM | " + user.getOrCreatePMChannel().getID());
        }
        maker.withColor(user);
        MessageMaker maker2 = new MessageMaker(channel).mustEmbed();
        maker2.getHeader().clear().append("📑 Feedback sent.");
        maker2.send();
    }
}