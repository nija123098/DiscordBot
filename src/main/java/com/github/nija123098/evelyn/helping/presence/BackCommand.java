package com.github.nija123098.evelyn.helping.presence;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author nija123098
 * @since 1.0.0
 */

public class BackCommand extends AbstractCommand {
    public BackCommand() {
        super(PresenceCommand.class, "back", "back, present", null, null, "Marks you as no longer afk");
    }
    @Command
    public void command(MessageMaker maker, User user, Guild guild) {
        ConfigHandler.setSetting(SelfMarkedAwayConfig.class, user, false);
        maker.append("Welcome back " + user.getDisplayName(guild) + "!");
    }
    private static final Set<String> NATURAL_TRIGGERS = new HashSet<>(Arrays.asList("back", "present", "i am back", "i'm back", "present", "back"));
    @Override
    public Set<String> getNaturalTriggers() {
        return NATURAL_TRIGGERS;
    }
    @Override
    public boolean prefixRequired() {
        return true;
    }
}
