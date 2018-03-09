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
public class AfkCommand extends AbstractCommand {
    public AfkCommand() {
        super(PresenceCommand.class, "afk", "brb, afk", null, "afk", "Marks you afk for a period of time");
    }
    @Command
    public void command(User user, MessageMaker maker, Guild guild){
        ConfigHandler.setSetting(SelfMarkedAwayConfig.class, user, false);
        maker.append(user.getDisplayName(guild) + " is now AFK.").mustEmbed();
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
