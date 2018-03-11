package com.github.nija123098.evelyn.moderation;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.perms.configs.specialperms.GuildSpecialPermsConfig;
import com.github.nija123098.evelyn.perms.configs.specialperms.SpecialPermsContainer;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class RestrictCommand extends AbstractCommand {
    public RestrictCommand() {
        super("restrict", ModuleLevel.ADMINISTRATIVE, null, null, "Restricts the bot to a single text channel for command invocation");
    }
    @Command
    public void command(@Argument(optional = true) Channel channel) {
        ConfigHandler.changeSetting(GuildSpecialPermsConfig.class, channel.getGuild(), container -> {
            if (container == null) container = new SpecialPermsContainer(channel.getGuild());
            container.restrict(channel);
            return container;
        });
    }
}
