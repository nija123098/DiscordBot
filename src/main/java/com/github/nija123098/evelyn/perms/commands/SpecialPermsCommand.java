package com.github.nija123098.evelyn.perms.commands;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.perms.BotRole;
import com.github.nija123098.evelyn.perms.configs.specialperms.GuildSpecialPermsConfig;
import com.github.nija123098.evelyn.perms.configs.specialperms.SpecialPermsContainer;

/**
 * Made by nija123098 on 6/13/2017.
 */
public class SpecialPermsCommand extends AbstractCommand {
    public SpecialPermsCommand() {
        super("commandadmin", BotRole.GUILD_TRUSTEE, ModuleLevel.ADMINISTRATIVE, "ca, specialperms, spc", null, "Helps set command approval and restrictions on Evelyn commands based on channel and ranks");
    }
    @Command
    public void command(Guild guild, @Argument Boolean enable){
        ConfigHandler.setSetting(GuildSpecialPermsConfig.class, guild, enable ? new SpecialPermsContainer(guild) : null);
    }

    @Override
    protected String getLocalUsages() {
        return "ca <boolean> // enables or disables the special command settings";
    }
}
