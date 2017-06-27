package com.github.kaaz.emily.helping.presence;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.VoiceChannel;
import com.github.kaaz.emily.util.Care;
import com.github.kaaz.emily.util.FormatHelper;

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
        Care.less(() -> Thread.sleep(10_000));
        List<String> list = channel.getConnectedUsers().stream().filter(user -> ConfigHandler.getSetting(SelfMarkedAwayConfig.class, user)).map(user -> user.getDisplayName(guild)).collect(Collectors.toList());
        if (list.isEmpty()) maker.append("Everyone is present.");
        else maker.appendRaw(FormatHelper.getList(list) + " are currently absent");
    }
}
