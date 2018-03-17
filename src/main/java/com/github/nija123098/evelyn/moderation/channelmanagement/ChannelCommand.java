package com.github.nija123098.evelyn.moderation.channelmanagement;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ContextType;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.configs.guild.GuildPrefixConfig;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordPermission;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.util.FormatHelper;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class ChannelCommand extends AbstractCommand {

    public ChannelCommand() {
        super("channel", ModuleLevel.ADMINISTRATIVE, null, null, "Check if I can perform channel changes, will also get info of mentioned channel");
    }

    @Command
    public void command(@Argument(optional = true, replacement = ContextType.NONE) Channel channel, MessageMaker maker, Guild guild) {
        String prefix = ConfigHandler.getSetting(GuildPrefixConfig.class, guild);
        maker.mustEmbed();
        if (channel == null) {
            if (DiscordClient.getOurUser().getPermissionsForGuild(guild).contains(DiscordPermission.MANAGE_CHANNELS)) {
                maker.getTitle().appendRaw("I can perform the following changes to channels in this server");
                maker.appendRaw("Create channel\nDelete channel\nChange channel name\nChange channel topic\nGet detailed channel info");
                maker.getFooter().appendRaw("use `" + prefix + "help channel` to see a overview of how to use the commands");
            } else {
                maker.getTitle().appendRaw("I can perform the following actions to channels in this server");
                maker.appendRaw("Get detailed channel info.");
                maker.getFooter().appendRaw("use `" + prefix + "help channel` to see a overview of how to use the commands");
            }
        } else {
            maker.getTitle().appendRaw(channel.getName());
            maker.appendRaw("\u200b");
            maker.getNewFieldPart().withInline(true).withBoth("Topic", ((channel.getTopic() == null || channel.getTopic().isEmpty()) ? "none" : channel.getTopic()));
            if (channel.getCategory() != null)maker.getNewFieldPart().withInline(true).withBoth("Category", channel.getCategory().getName());
            maker.getNewFieldPart().withInline(true).withBoth("ID", channel.getID());
            maker.getNewFieldPart().withInline(true).withBoth("NSFW", "" + channel.isNSFW());
            maker.getNewFieldPart().withInline(true).withBoth("Users with access", (channel.getUsersHere().size() <= 12 ? FormatHelper.makeUserTable(channel.getUsersHere(), 23, 2) : "" + channel.getUsersHere().size()));
        }
    }
}