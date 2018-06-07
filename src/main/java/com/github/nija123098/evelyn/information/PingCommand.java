package com.github.nija123098.evelyn.information;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Message;
import com.github.nija123098.evelyn.util.EmoticonHelper;
import com.github.nija123098.evelyn.util.Log;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class PingCommand extends AbstractCommand {
    public PingCommand() {
        super("ping", ModuleLevel.INFO, null, "ping_pong", "Checks if the bot is responding");
    }
    @Command
    public static void command(MessageMaker helper, Message message) {
        helper.appendRaw(EmoticonHelper.getChars("outbox_tray", false) + " ").append("Checking ping").send();
        Message response = helper.sentMessage();
        if (response == null) {
            Log.log("Unable to send response, message attempted to be sent but failed for ping");
            return;
        }
        long time = response.getTime() - message.getTime();
        Log.log("Ping is " + time);
        helper.forceCompile().getHeader().clear();
        helper.appendRaw(EmoticonHelper.getChars("inbox_tray", false) + " ").append("ping is " + time + "ms");
    }
}
