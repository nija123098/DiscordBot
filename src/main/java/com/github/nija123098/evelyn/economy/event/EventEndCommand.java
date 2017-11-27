package com.github.nija123098.evelyn.economy.event;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.GlobalConfigurable;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.economy.event.configs.EventActiveConfig;

/**
 * Written by Soarnir 18/11/17
 */

public class EventEndCommand extends AbstractCommand {
    public EventEndCommand() {
        super("end event", ModuleLevel.BOT_ADMINISTRATIVE, null, null, "end the current event");
    }

    @Command
    public void command(MessageMaker maker) {
        ConfigHandler.setSetting(EventActiveConfig.class, GlobalConfigurable.GLOBAL, false);
        maker.appendRaw("Event finished");
    }
}
