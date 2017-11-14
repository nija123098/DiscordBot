package com.github.nija123098.evelyn.botmanagement;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient;
import com.github.nija123098.evelyn.moderation.logging.BotChannelConfig;

import java.util.Set;
import java.util.stream.Collectors;

public class AnnounceCommand extends AbstractCommand {
    public AnnounceCommand() {
        super("announce", ModuleLevel.BOT_ADMINISTRATIVE, null, null, "Sends a message to every bot channel");
    }
    @Command
    public void command(@Argument(info = "The stuff to say") String text){
        MessageMaker maker = new MessageMaker((Channel) null);
        Set<Channel> channels = DiscordClient.getGuilds().stream().map((guild -> ConfigHandler.getSetting(BotChannelConfig.class, guild))).collect(Collectors.toSet());
        channels.remove(null);
        for (Channel channel : channels) {
                maker.forceCompile().withChannel(channel).appendRaw(text).send();
                maker.forceCompile().clearMessage();
        }
    }
}
