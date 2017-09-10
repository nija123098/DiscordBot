package com.github.nija123098.evelyn.helping.presence;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.VoiceChannel;
import com.github.nija123098.evelyn.util.Care;
import com.github.nija123098.evelyn.util.FormatHelper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Made by nija123098 on 6/27/2017.
 */
public class RollCallCommand extends AbstractCommand {
    public RollCallCommand() {
        super(PresenceCommand.class, "rollcall", null, null, null, "Waits for ten seconds and then checks presences for each user in the voice channel");
    }
    @Command
    public void command(VoiceChannel channel, MessageMaker maker, Guild guild){
        List<String> list = null;
        for (int i = 0; i < 5; i++) {
            list = channel.getConnectedUsers().stream().filter(user -> !user.isBot()).filter(user -> ConfigHandler.getSetting(SelfMarkedAwayConfig.class, user)).map(user -> user.getDisplayName(guild)).collect(Collectors.toList());
            if (list.isEmpty()) {
                maker.append("Everyone is present.");
                return;
            }
            Care.lessSleep(2000);
        }
        maker.appendRaw(FormatHelper.getList(list) + " are currently absent");
    }
}
