package com.github.nija123098.evelyn.helping;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;

/**
 * @author Dxeo
 * @since 1.0.0
 */
public class EmbedCommand extends AbstractCommand {

    //constructor
    public EmbedCommand() {
        super("embed", ModuleLevel.HELPER, null, null, null);
    }

    @Command
    public void embed(@Argument String s, MessageMaker maker) {

        maker.append(s).mustEmbed();

    }
}