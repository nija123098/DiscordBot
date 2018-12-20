package com.github.nija123098.evelyn.information.descriptions;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ContextType;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.command.annotations.Context;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.exception.ContextException;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class SetDescriptionCommand extends AbstractCommand {
    public SetDescriptionCommand() {
        super(DescriptionCommand.class, "set", "setdescription", null, null, "Sets info about yourself");
    }
    @Command
    public void command(@Argument(optional = true, replacement = ContextType.NONE, info = "server only") Boolean serverOnly, @Argument(info = "Your info") String info, @Context(softFail = true) Guild guild, User user) {
        if (serverOnly == null) serverOnly = false;
        if (serverOnly && guild == null) throw new ContextException("In order to set server only information you must be in a server");
        if (serverOnly) ConfigHandler.setSetting(GuildUserDescriptionConfig.class, GuildUser.getGuildUser(guild, user), info);
        else ConfigHandler.setSetting(UserDescriptionConfig.class, user, info);
    }
}
