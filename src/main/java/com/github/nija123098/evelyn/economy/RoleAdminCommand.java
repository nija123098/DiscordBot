package com.github.nija123098.evelyn.economy;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.configs.guild.GuildPrefixConfig;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class RoleAdminCommand extends AbstractCommand {
    public RoleAdminCommand() {
        super("roleadmin", ModuleLevel.ADMINISTRATIVE, null, null, "Management of buy-able roles");
    }
    @Command
    public void command(MessageMaker maker, Guild guild) {

        maker.append("This command is now deprecated, please use the config command to set this.\n");
        maker.appendAlternate(true, "For example this sets the `Noob` role as self assignable with `!getrole`.\n");
        maker.appendRaw("```" + ConfigHandler.getSetting(GuildPrefixConfig.class, guild) + "config set role_buy Noob 0```\n");
        maker.appendAlternate(true, "For example this sets the `Haxor` role as purchasable for 5 cookies.\n");
        maker.appendRaw("```" + ConfigHandler.getSetting(GuildPrefixConfig.class, guild) + "config set role_buy Haxor 5```");
    }
}
