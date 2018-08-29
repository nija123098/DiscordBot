package com.github.nija123098.evelyn.information;

import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.wrappers.Message;
import sx.blah.discord.util.RequestBuffer;

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
    public void command(Message message, @Argument(optional = true) Integer integer) {
        if (integer == null ? ConfigProvider.BOT_SETTINGS.ghostModeEnabled() : !integer.equals(ConfigProvider.BOT_SETTINGS.instanceId())) return;
        Instant sysTime = Instant.now();
        Instant messageTime = message.getCreationDate();
        long timeDiff = ChronoUnit.MILLIS.between(messageTime, sysTime);
        RequestBuffer.request(() -> message.getChannel().channel().sendMessage(String.valueOf(timeDiff)));
    }
}
