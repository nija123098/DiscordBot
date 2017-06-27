package com.github.kaaz.emily.automoderation.modaction;

import com.github.kaaz.emily.automoderation.modaction.support.AbstractModAction;
import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.annotations.Argument;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.config.GuildUser;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.User;

/**
 * Made by nija123098 on 5/13/2017.
 */
public class ModActionCaseCommand extends AbstractCommand {
    public ModActionCaseCommand() {
        super(ModActionCommand.class, "case", "case", null, null, "Allows updating of cases for mod actions");
    }
    @Command
    public void command(MessageMaker maker, Guild guild, User user, @Argument(optional = true, info = "The case number") Integer integer, @Argument(info = "The reason") String reason){
        if (integer == null) integer = AbstractModAction.lastCase(GuildUser.getGuildUser(guild, user));
        AbstractModAction.updateCase(guild, integer, reason);
    }
}
