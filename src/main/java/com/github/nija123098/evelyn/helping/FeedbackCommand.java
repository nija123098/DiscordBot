package com.github.nija123098.evelyn.helping;

import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.command.annotations.Context;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.Message;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

/**
 * @author Dxeo
 * @since 1.0.0
 */
public class FeedbackCommand extends AbstractCommand {
    public FeedbackCommand() {
        super("feedback", null, "feedback, bug", null, "Send a bug report or feedback to my devs.  Image attachments can also be reported.");
    }

    @Command
    public void bug(MessageMaker response, Channel channel, @Context(softFail = true) Guild guild, User user, @Argument String msg, Message message) {
        MessageMaker report = new MessageMaker(channel);
        report.withChannel(Channel.getChannel(ConfigProvider.BOT_SETTINGS.feedbackChannel()));
        report.withThumb(message.getContent().toLowerCase().contains("bug") ? ConfigProvider.URLS.bugThumb() : ConfigProvider.URLS.feedbackThumb());
        report.getHeader().clear().append(msg);
        report.withAuthor(user);
        report.withAuthorIcon(user.getAvatarURL());
        report.withTimestamp(System.currentTimeMillis());
        if (guild != null) report.getNote().clear().append(guild.getName() + " | " + guild.getID());
        else report.getNote().clear().append("DM | " + user.getOrCreatePMChannel().getID());
        report.withColor(user);
        if (!message.getAttachments().isEmpty()) {
            report.withImage(message.getAttachments().get(0).getUrl());
        }
        report.send();
        response.append("Report sent.  Thank you for the information.  Have a good day!");
    }
}