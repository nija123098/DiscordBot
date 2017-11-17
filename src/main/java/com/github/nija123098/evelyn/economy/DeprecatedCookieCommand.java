package com.github.nija123098.evelyn.economy;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;

/**
 * Written by Soarnir 16/11/17
 */

public class DeprecatedCookieCommand extends AbstractCommand {
    public DeprecatedCookieCommand() {
        super("cookie", null, null, null, "this is a deprecated command which serves little purpose");
    }

    @Command
    public void command(MessageMaker maker) {
        maker.appendRaw("Hi! I recently shifted to a new kind of system, from now on you'll need to use `!claim` to claim your cookies or guild set currency");
    }
}
