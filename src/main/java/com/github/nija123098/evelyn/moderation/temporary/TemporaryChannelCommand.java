package com.github.nija123098.evelyn.moderation.temporary;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ContextType;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.wrappers.Category;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.util.Time;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class TemporaryChannelCommand extends AbstractCommand {
    public TemporaryChannelCommand() {
        super("tempchannel", ModuleLevel.ADMINISTRATIVE, "temporary channel, temporarychannel, tempc, tempchan", null, "Makes a temporary channel which is deleted when activity dwindles");
    }
    @Command
    public static void command(@Argument(optional = true, replacement = ContextType.NONE, info = "if it's a text channel") Boolean textChannel, @Argument(info = "The time until expire", replacement = ContextType.NONE) Time time, @Argument(info = "name") String name, Guild guild) {
        if (textChannel == null) textChannel = true;
        long schedule = time == null ? 30_000 + System.currentTimeMillis() : time.schedualed();
        Channel channel = textChannel ? guild.createChannel(name) : guild.createVoiceChannel(name);
        Category category = ConfigHandler.getSetting(TemporaryChannelCategoryConfig.class, guild);
        if (category != null) channel.changeCategory(category);
        ConfigHandler.setSetting(TemporaryChannelConfig.class, channel, schedule);
    }
}
