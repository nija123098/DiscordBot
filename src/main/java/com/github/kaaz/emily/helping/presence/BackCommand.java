package com.github.kaaz.emily.helping.presence;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.User;

/**
 * Made by nija123098 on 6/23/2017.
 */
public class BackCommand extends AbstractCommand {
    public BackCommand() {
        super(PresenceCommand.class, "back", "i'm back, i am back", null, null, "Marks you as no longer afk");
    }
    @Command
    public void command(MessageMaker maker, User user){
        ConfigHandler.setSetting(SelfMarkedAwayConfig.class, user, 0L);
    }
}
