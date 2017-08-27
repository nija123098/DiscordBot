package com.github.kaaz.emily.automoderation;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Argument;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.discordobjects.wrappers.Channel;
import com.github.kaaz.emily.perms.configs.specialperms.GuildSpecialPermsConfig;

public class RestrictCommand extends AbstractCommand {
    public RestrictCommand() {
        super("restrict", ModuleLevel.ADMINISTRATIVE, null, null, "Restricts the bot to a single text channel");
    }
    @Command
    public void command(@Argument(optional = true) Channel channel){
        ConfigHandler.alterSetting(GuildSpecialPermsConfig.class, channel.getGuild(), container -> container.restrict(channel));
    }
}
