package com.github.kaaz.emily.helping.presence;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.discordobjects.wrappers.User;

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
