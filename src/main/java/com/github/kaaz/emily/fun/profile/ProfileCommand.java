package com.github.kaaz.emily.fun.profile;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Argument;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.User;

public class ProfileCommand extends AbstractCommand {
    public ProfileCommand() {
        super("profile", ModuleLevel.FUN, null, null, "Shows info on a user");
    }
    @Command
    public void command(@Argument(optional = true) User user, MessageMaker maker){
        maker.withFile(ProfileMaker.getProfileIcon(user));
    }
}
