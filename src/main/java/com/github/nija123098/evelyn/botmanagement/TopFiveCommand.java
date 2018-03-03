package com.github.nija123098.evelyn.botmanagement;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class TopFiveCommand extends AbstractCommand {

    public TopFiveCommand() {
        super("topfive", ModuleLevel.BOT_ADMINISTRATIVE, null, null, "get the top five command users");
    }

    @Command
    public void command() {

    }
}