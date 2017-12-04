package com.github.nija123098.evelyn.fun.profile;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

/**
 * @author nija123098
 * @since 1.0.0
 */
// Making this abstract disables it, it's now the recommended way to disable commands in the code, because I determine that.
public abstract class ProfileCommand extends AbstractCommand {
    public ProfileCommand() {
        super("profile", ModuleLevel.FUN, null, null, "Shows info on a user");
    }
    @Command
    public void command(@Argument(optional = true) User user, MessageMaker maker){
        maker.withFile(ProfileMaker.getProfileIcon(user));
    }
}
