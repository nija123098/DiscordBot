package com.github.kaaz.emily.automoderation.teams;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.command.commands.HelpCommand;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.Channel;
import com.github.kaaz.emily.discordobjects.wrappers.User;

/**
 * Made by nija123098 on 7/23/2017.
 */
public class TeamCommand extends AbstractCommand {
    public TeamCommand() {
        super("team", ModuleLevel.ADMINISTRATIVE, null, null, "Shows help the team commands");
    }
    @Command
    public void command(MessageMaker maker, User user, Channel channel){
        HelpCommand.command(this, maker, user, channel, null);
    }
}
