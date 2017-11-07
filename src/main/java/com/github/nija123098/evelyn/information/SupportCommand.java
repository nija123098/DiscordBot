package com.github.nija123098.evelyn.information;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;

/**
 * Made by nija123098 on 5/30/2017.
 */
public class SupportCommand extends AbstractCommand {
    public SupportCommand() {
        super("support", ModuleLevel.INFO, "discord", null, "Brings you to my support server");
    }
    @Command
    public void command(MessageMaker maker){
        maker.withDeleteDelay(30_000L).append("If you need help or have questions/suggestions feel free to drop by at ").appendRaw("https://discord.gg/UW5X5BU");
    }
}
