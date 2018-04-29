package com.github.nija123098.evelyn.helping.reward;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.command.annotations.Context;
import com.github.nija123098.evelyn.command.commands.HelpCommand;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

/**
 * @author Dxeo
 * @since 1.0.0
 */
public class RewardCommand extends AbstractCommand {
    public RewardCommand() {
        super("reward", ModuleLevel.BOT_ADMINISTRATIVE, null, null, "Show the help for reward.");
    }

    @Command
    public void reward(MessageMaker maker, @Context(softFail = true) Guild guild, User user, Channel channel) {
        HelpCommand.command(this, maker, user, channel, guild, this.getModule(), this.getName());
    }
}