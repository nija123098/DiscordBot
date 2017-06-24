package com.github.kaaz.emily.helping.presence;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.util.FormatHelper;

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
        maker.append("Please use one of these sub-commands\n").appendRaw(FormatHelper.makeTable(this.getSubCommands().stream().map(AbstractCommand::getName).collect(Collectors.toList())));
    }
    @Override
    public boolean prefixRequired() {
        return true;
    }
}
