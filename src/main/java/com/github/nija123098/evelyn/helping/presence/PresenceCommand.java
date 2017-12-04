package com.github.nija123098.evelyn.helping.presence;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;

import static com.github.nija123098.evelyn.command.ModuleLevel.HELPER;
import static com.github.nija123098.evelyn.util.FormatHelper.makeTable;
import static java.util.stream.Collectors.toList;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class PresenceCommand extends AbstractCommand {
    public PresenceCommand() {
        super("presence", HELPER, null, null, "Helps track the presence of a user");
    }

    @Command
    public void command(MessageMaker maker) {
        maker.append("Please use one of these sub-commands:\n").appendRaw(makeTable(this.getSubCommands().stream().map(abstractCommand -> abstractCommand.getName() + "    ").collect(toList())));
    }
}
