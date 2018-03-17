package com.github.nija123098.evelyn.helping.send;

import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;

/**
 * @author Dxeo
 * @since 1.0.0
 */
public class SendFeedbackCommand extends AbstractCommand {
    public SendFeedbackCommand() {
        super(SendCommand.class, "feedback", null, null, null, "Send feedback to the bot devs");
    }

    @Command
    public void feedback(MessageMaker maker, GuildUser guser, @Argument String msg) {

        maker.withChannel(Channel.getChannel(ConfigProvider.BOT_SETTINGS.feedbackChannel()));
        maker.withThumb(ConfigProvider.URLS.feedbackThumb());
        maker.getHeader().clear().append(msg);
        maker.withAuthor(guser.getUser());
        maker.withAuthorIcon(guser.getUser().getAvatarURL());
        maker.withTimestamp(System.currentTimeMillis());
        maker.getNote().clear().append(guser.getGuild().getName() + " | " + guser.getGuild().getID());
        maker.withColor(guser.getUser());
    }
}