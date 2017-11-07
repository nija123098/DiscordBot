package com.github.nija123098.evelyn.helping.presence;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Made by nija123098 on 6/23/2017.
 */
public class AfkCommand extends AbstractCommand {
    public AfkCommand() {
        super(PresenceCommand.class, "afk", "brb, afk", null, "afk", "Marks you afk for a period of time");
    }
    @Command
    public void command(User user){
        ConfigHandler.setSetting(SelfMarkedAwayConfig.class, user, false);
    }
    private static final Set<String> NATURAL_TRIGGERS = new HashSet<>(Arrays.asList("afk", "brb", "ill be right back", "i will be right back"));
    @Override
    public Set<String> getNaturalTriggers(){
        return NATURAL_TRIGGERS;
    }
    @Override
    public boolean prefixRequired() {
        return true;
    }
}
