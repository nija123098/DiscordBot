package com.github.nija123098.evelyn.economy;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;

/**
 * Made by nija123098 on 5/22/2017.
 */
public class RoleAdminCommand extends AbstractCommand {
    public RoleAdminCommand() {
        super("roleadmin", ModuleLevel.ADMINISTRATIVE, null, null, "Management of buy-able roles");
    }
    @Command
    public void command(MessageMaker maker){
        maker.append("This command is now deprecated, please use the config command to set this.\n");
        maker.appendAlternate(true, "For example this sets the `Noob` role as self assignable.\n");
        maker.appendRaw("```@Emily config Noob role_buy 0```\n");
        maker.appendAlternate(true, "For example this sets the `Haxor` role as purchasable for 5 cookies.\n");
        maker.appendRaw("```@Emily config Haxor role_buy 5```");
    }
}
