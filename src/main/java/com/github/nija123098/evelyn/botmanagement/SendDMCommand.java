package com.github.nija123098.evelyn.botmanagement;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class SendDMCommand extends AbstractCommand {

    public SendDMCommand() {
        super("senddm", ModuleLevel.BOT_ADMINISTRATIVE, null, null, "send a user a DM via Evelyn");
    }

    @Command
    public void command(@Argument User user, @Argument String message) {
        MessageMaker maker = new MessageMaker(user.getOrCreatePMChannel());
        maker.mustEmbed();
        maker.getTitle().appendRaw("A message from my developers");
        maker.appendRaw(message);
    }
}