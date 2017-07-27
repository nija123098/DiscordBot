package com.github.kaaz.emily.information;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Argument;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.util.EmoticonHelper;

/**
 * Made by nija123098 on 3/30/2017.
 */
public class PingCommand extends AbstractCommand {
    public PingCommand() {
        super("ping", ModuleLevel.INFO, null, "ping_pong", "Checks if the bot is responding");
    }
    @Command
    public void command(@Argument(optional = true) String argument, MessageMaker helper){
        long time = System.currentTimeMillis();
        helper.appendRaw(EmoticonHelper.getChars("outbox_tray", false) + " ").append("Checking ping").send();
        helper.forceCompile().getHeader().clear();
        helper.appendRaw(EmoticonHelper.getChars("inbox_tray", false) + " ").append("ping is " + (System.currentTimeMillis() - time) + "ms");
    }
}
