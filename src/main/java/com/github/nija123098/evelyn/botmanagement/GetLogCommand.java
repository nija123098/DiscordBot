package com.github.nija123098.evelyn.botmanagement;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.util.Log;

/**
 * @author Dxeo
 * @since 1.0.0
 */
public class GetLogCommand extends AbstractCommand {

    //constructor
    public GetLogCommand() {
        super("getlog", ModuleLevel.DEVELOPMENT, "gl", null, null);
    }

    @Command
    public void getlogcommand(MessageMaker maker) {

        maker.withDM().withFile(Log.LOG_PATH.toFile());

    }
}