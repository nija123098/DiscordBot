package com.github.nija123098.evelyn.economy;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.GlobalConfigurable;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.economy.configs.*;

import java.time.Instant;

/**
 * Written by Soarnir 18/11/17
 */

public class EventCreateCommand extends AbstractCommand {
    public EventCreateCommand() {
        super("create event", ModuleLevel.BOT_ADMINISTRATIVE, null, null, "create events");
    }

    @Command
    public void command(@Argument String arg, MessageMaker maker, User user) {
        String[] args = arg.split(":");
        if (Boolean.valueOf(args[0])) {
            ConfigHandler.setSetting(EventNameConfig.class, GlobalConfigurable.GLOBAL, String.valueOf(args[0]));
            ConfigHandler.setSetting(EventBonusConfig.class, GlobalConfigurable.GLOBAL, Integer.valueOf(args[1]));
            maker.appendRaw("Created a new event called: " + args[1] + " with a " + args[2] + " bonus.");
            ConfigHandler.setSetting(EventStartConfig.class, GlobalConfigurable.GLOBAL, Instant.parse(args[2] + "T00:00:00Z").toString());
            ConfigHandler.setSetting(EventEndConfig.class, GlobalConfigurable.GLOBAL, Instant.parse(args[3] + "T00:00:00Z").toString());
        } else {
            maker.appendRaw("successfully finished the event");
        }
    }

    @Override
    protected String getLocalUsages() {
        return "#  event create <event name>:<event bonus>:<event start>:<event end> // event create Christmas:1000:2017-12-24:2017-12-25";
    }
}
