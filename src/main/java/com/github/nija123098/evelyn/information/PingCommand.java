package com.github.nija123098.evelyn.information;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient;
import com.github.nija123098.evelyn.util.EmoticonHelper;
import com.github.nija123098.evelyn.util.Log;

/**
 * Made by nija123098 on 3/30/2017.
 */
public class PingCommand extends AbstractCommand {
    public PingCommand() {
        super("ping", ModuleLevel.INFO, null, "ping_pong", "Checks if the bot is responding");
    }
    @Command
    public void command(MessageMaker helper){
        long time = System.currentTimeMillis();
        helper.appendRaw(EmoticonHelper.getChars("outbox_tray", false) + " ").append("Checking ping").send();
        time = System.currentTimeMillis() - time;
        Log.log("Ping is " + time);
        helper.forceCompile().getHeader().clear();
        helper.appendRaw(EmoticonHelper.getChars("inbox_tray", false) + " ").append("ping is " + time + "ms");
    }
}
