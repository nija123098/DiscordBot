package com.github.nija123098.evelyn.moderation.modaction;

import com.github.nija123098.evelyn.moderation.modaction.support.AbstractModAction;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

/**
 * Made by nija123098 on 5/13/2017.
 */
public class ModActionCaseCommand extends AbstractCommand {
    public ModActionCaseCommand() {
        super(ModActionCommand.class, "case", "case", null, null, "Allows updating of cases for mod actions");
    }
    @Command
    public void command(Guild guild, User user, @Argument(optional = true, info = "The case number") Integer integer, @Argument(info = "The reason") String reason){
        if (integer == null) integer = AbstractModAction.lastCase(GuildUser.getGuildUser(guild, user));
        AbstractModAction.updateCase(guild, integer, reason);
    }
}
