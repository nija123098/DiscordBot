package com.github.nija123098.evelyn.information;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Message;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * @author Celestialdeath99
 * @since 1.0.0
 */
public class PingBotCommand extends AbstractCommand {

    public PingBotCommand() {
        super(PingCommand.class, "bot", null, null, null, "A plain version of the ping command for bots.");
    }

    @Command
    public void command(MessageMaker maker, Message message) {
        Instant sysTime = Instant.now();
        Instant messageTime = message.getCreationDate();
        long timeDiff = ChronoUnit.MILLIS.between(messageTime, sysTime);
        maker.shouldEmbed(false).appendRaw(String.valueOf(timeDiff));
    }
}
