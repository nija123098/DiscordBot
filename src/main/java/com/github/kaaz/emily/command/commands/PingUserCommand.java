package com.github.kaaz.emily.command.commands;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.command.anotations.Context;
import com.github.kaaz.emily.command.anotations.Convert;
import com.github.kaaz.emily.discordobjects.helpers.MessageHelper;
import com.github.kaaz.emily.discordobjects.wrappers.Channel;
import com.github.kaaz.emily.discordobjects.wrappers.User;

/**
 * Made by nija123098 on 3/31/2017.
 */
public class PingUserCommand extends AbstractCommand {
    public PingUserCommand(PingCommand command) {
        super(command, "user", null, null, null, null, null);
    }
    @Command
    public void command(@Convert User user, @Convert User u2, @Convert Channel channel, @Context MessageHelper helper, @Context String args){
        helper.appendTranslation(user.mention() + " ==> " + u2.mention() + "\nMessage:    " + args).withChannel(channel);
    }
}
