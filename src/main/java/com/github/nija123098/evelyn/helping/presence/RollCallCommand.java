package com.github.nija123098.evelyn.helping.presence;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.VoiceChannel;

import java.util.List;

import static com.github.nija123098.evelyn.config.ConfigHandler.getSetting;
import static com.github.nija123098.evelyn.util.Care.lessSleep;
import static com.github.nija123098.evelyn.util.FormatHelper.getList;
import static java.util.stream.Collectors.toList;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class RollCallCommand extends AbstractCommand {
    public RollCallCommand() {
        super(PresenceCommand.class, "rollcall", null, null, null, "Waits for ten seconds and then checks presences for each user in the voice channel");
    }

    @Command
    public void command(VoiceChannel channel, MessageMaker maker, Guild guild) {
        List<String> list = null;
        for (int i = 0; i < 5; i++) {
            list = channel.getConnectedUsers().stream().filter(user -> !user.isBot()).filter(user -> getSetting(SelfMarkedAwayConfig.class, user)).map(user -> user.getDisplayName(guild)).collect(toList());
            if (list.isEmpty()) {
                maker.append("Everyone is present.");
                return;
            }
            lessSleep(2000);
        }
        maker.appendRaw(getList(list) + " are currently absent");
    }
}
