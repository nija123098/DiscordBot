package com.github.nija123098.evelyn.automoderation;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.perms.configs.specialperms.GuildSpecialPermsConfig;

public class RestrictCommand extends AbstractCommand {
    public RestrictCommand() {
        super("restrict", ModuleLevel.ADMINISTRATIVE, null, null, "Restricts the bot to a single text channel");
    }
    @Command
    public void command(@Argument(optional = true) Channel channel){
        ConfigHandler.alterSetting(GuildSpecialPermsConfig.class, channel.getGuild(), container -> container.restrict(channel));
    }
}
