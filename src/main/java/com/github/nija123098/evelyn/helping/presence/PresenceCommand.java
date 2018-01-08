package com.github.nija123098.evelyn.helping.presence;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.util.FormatHelper;

import java.util.stream.Collectors;

/**
 * Made by nija123098 on 6/23/2017.
 */
public class PresenceCommand extends AbstractCommand {
    public PresenceCommand() {
        super("presence", ModuleLevel.HELPER, null, null, "Helps track the presence of a user");
    }
    @Command
    public void command(MessageMaker maker){
        maker.append("Please use one of these sub-commands:\n").appendRaw(FormatHelper.makeTable(this.getSubCommands().stream().map(abstractCommand -> abstractCommand.getName() + "    ").collect(Collectors.toList())));
    }
}
