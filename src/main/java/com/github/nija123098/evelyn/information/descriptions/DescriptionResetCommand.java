package com.github.nija123098.evelyn.information.descriptions;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.command.annotations.Context;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.exeption.ContextException;

/**
 * @author nija123098
 */
public class DescriptionResetCommand extends AbstractCommand {
    public DescriptionResetCommand() {
        super(DescriptionCommand.class, "reset", null, null, null, "Resets your own description of yourself");
    }
    @Command
    public void command(@Argument(optional = true, info = "if resetting the server specific description") Boolean serverSpecific, User user, @Context(softFail = true) Guild guild){
        if (serverSpecific == null || serverSpecific) {
            if (guild == null) throw new ContextException("You have to be in a server to reset server specific descriptions");
            ConfigHandler.reset(GuildUserDescriptionConfig.class, GuildUser.getGuildUser(guild, user));
        }else ConfigHandler.reset(UserDescriptionConfig.class, user);
    }
}
