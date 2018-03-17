package com.github.nija123098.evelyn.helping.reward;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.command.commands.HelpCommand;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;

/**
 * @author Dxeo
 * @since 1.0.0
 */
public class RewardCommand extends AbstractCommand {
    public RewardCommand() {
        super("reward", ModuleLevel.BOT_ADMINISTRATIVE, null, null, "Show the help for reward.");
    }

    @Command
    public void reward(MessageMaker maker, GuildUser guser, Channel channel) {
        HelpCommand.command(this, maker, guser.getUser(), channel, guser.getGuild(), this.getModule(), this.getName());
    }
}