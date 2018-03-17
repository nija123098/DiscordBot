package com.github.nija123098.evelyn.helping.send;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.CommandHandler;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.command.commands.HelpCommand;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Message;
import com.github.nija123098.evelyn.tag.Tag;
import com.github.nija123098.evelyn.tag.Tags;

/**
 * @author Dxeo
 * @since 1.0.0
 */
@Tags(value = {Tag.HELPFUL})
public class SendCommand extends AbstractCommand {
    public SendCommand() {
        super("send", ModuleLevel.HELPER, null, null, "Display send help.");
    }

    @Command
    public void send(MessageMaker maker, GuildUser guser, Channel channel) {
        HelpCommand.command(this ,maker, guser.getUser(), channel, guser.getGuild(), this.getModule(), this.getName());
    }
}