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
import com.github.nija123098.evelyn.util.FormatHelper;

import java.awt.*;
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
        maker.withAuthorIcon(DiscordClient.getOurUser().getAvatarURL());
        maker.withThumb("https://cdn.discordapp.com/attachments/374538747229896704/384862519354982403/38153-200.png");
        maker.getFooter().appendRaw("Click " + FormatHelper.embedLink("here","https://discord.gg/UW5X5BU") + " to join the support server.");
        maker.withColor(new Color(175, 30,5));
        for (Channel channel : channels) {
            maker.getAuthorName().appendRaw(DiscordClient.getOurUser().getDisplayName(channel.getGuild()) + " Announcement");
            maker.forceCompile().withChannel(channel).appendRaw(text).send();
            maker.forceCompile().clearMessage();
        }
    }
}
