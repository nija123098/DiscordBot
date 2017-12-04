package com.github.nija123098.evelyn.perms.commands;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.perms.configs.specialperms.GuildSpecialPermsConfig;
import com.github.nija123098.evelyn.perms.configs.specialperms.SpecialPermsContainer;

import static com.github.nija123098.evelyn.command.ModuleLevel.ADMINISTRATIVE;
import static com.github.nija123098.evelyn.config.ConfigHandler.setSetting;
import static com.github.nija123098.evelyn.perms.BotRole.GUILD_TRUSTEE;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class SpecialPermsCommand extends AbstractCommand {
    public SpecialPermsCommand() {
        super("commandadmin", GUILD_TRUSTEE, ADMINISTRATIVE, "ca, specialperms, spc", null, "Helps set command approval and restrictions on Evelyn commands based on channel and ranks");
    }

    @Command
    public void command(Guild guild, @Argument Boolean enable) {
        setSetting(GuildSpecialPermsConfig.class, guild, enable ? new SpecialPermsContainer(guild) : null);
    }

    @Override
    protected String getLocalUsages() {
        return "#  ca <boolean> // enables or disables the special command settings";
    }
}
