package com.github.nija123098.evelyn.helping.send;

import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import com.github.nija123098.evelyn.botconfiguration.configinterfaces.botconfig.BotSettings;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.command.annotations.Context;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.Message;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.util.EmoticonHelper;
import com.github.nija123098.evelyn.util.FormatHelper;

/**
 * @author Dxeo
 * @since 1.0.0
 */
public class SendBugCommand extends AbstractCommand {
    public SendBugCommand() {
        super(SendCommand.class, "bug", null, null, "b", "Send a bug report to the devs, you can attach an image to the command message.");
    }

    @Command
    public void bug(MessageMaker maker, Channel channel, @Context(softFail = true) Guild guild, User user, @Argument String msg, Message message) {

        maker.withChannel(Channel.getChannel(ConfigProvider.BOT_SETTINGS.bugChannel()));
        maker.withThumb(ConfigProvider.URLS.bugThumb());
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
        if (!message.getAttachments().isEmpty()) {
            maker.withImage(message.getAttachments().get(0).getUrl());
        }
        MessageMaker maker2 = new MessageMaker(channel).mustEmbed();
        maker2.getHeader().clear().append("🐞 Bug Report sent.");
        maker2.send();
    }
}