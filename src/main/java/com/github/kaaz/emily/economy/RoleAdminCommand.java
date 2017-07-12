package com.github.kaaz.emily.economy;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;

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
        maker.appendAlternate(true, "For example this sets the `Haxor` role as purchasable for 5.2 currency.\n");
        maker.appendRaw("```@Emily config Haxor role_buy 5.2```");
    }
}
