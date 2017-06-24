package com.github.kaaz.emily.helping.presence;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ContextType;
import com.github.kaaz.emily.command.anotations.Argument;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.util.Time;

/**
 * Made by nija123098 on 6/23/2017.
 */
public class AfkCommand extends AbstractCommand {
    public AfkCommand() {
        super(PresenceCommand.class, "afk", "brb, i'll be right back, i will be right back", null, null, "Marks you afk for a period of time");
    }
    @Command
    public void command(@Argument(optional = true, replacement = ContextType.NONE) Time time, User user, MessageMaker maker){
        if (time == null) ConfigHandler.setSetting(SelfMarkedAwayConfig.class, user, null);
        else ConfigHandler.setSetting(SelfMarkedAwayConfig.class, user, time.schedualed());
    }
}
