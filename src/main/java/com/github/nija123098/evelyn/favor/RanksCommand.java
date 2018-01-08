package com.github.nija123098.evelyn.favor;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;

public class RanksCommand extends AbstractCommand {
    public RanksCommand() {
        super("ranks", ModuleLevel.ADMINISTRATIVE, null, null, "Stuff");
    }
    @Command
    public void command(MessageMaker maker){
        maker.append("If you see this report it to nija for your prize.");
    }
}
