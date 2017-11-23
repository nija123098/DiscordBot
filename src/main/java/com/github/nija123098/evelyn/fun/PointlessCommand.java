package com.github.nija123098.evelyn.fun;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.util.EmoticonHelper;

/**
 * Written by Soarnir 23/11/17
 */

public class PointlessCommand extends AbstractCommand {
    public PointlessCommand() {
        super("pointless", ModuleLevel.FUN, null, null, "lel");
    }

    @Command
    public void command(MessageMaker maker) {
        maker.appendRaw(EmoticonHelper.getEmoji("EMPTY").toString());
        maker.withReaction("red_circle");
    }
}
