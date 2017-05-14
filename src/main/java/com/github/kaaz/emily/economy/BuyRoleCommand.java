package com.github.kaaz.emily.economy;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.command.anotations.Argument;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.GuildUser;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.Role;
import com.github.kaaz.emily.economy.configs.CurrentMoneyConfig;
import com.github.kaaz.emily.economy.configs.RoleBuyConfig;
import com.github.kaaz.emily.exeption.ArgumentException;

/**
 * Made by nija123098 on 5/5/2017.
 */
public class BuyRoleCommand extends AbstractCommand {
    public BuyRoleCommand() {
        super("buyrole", ModuleLevel.ECONOMY, null, null, "Allows the buying of roles with guild based money.");
    }
    @Command
    public void command(@Argument Role role, Guild guild, GuildUser guildUser){
        Float f = ConfigHandler.getSetting(RoleBuyConfig.class, guild).get(role);
        if (f == null) throw new ArgumentException("You can not buy that role");
        Float c = ConfigHandler.getSetting(CurrentMoneyConfig.class, guildUser);
        if (c < f) throw new ArgumentException("You must have " + f + " currency to buy that role.  Current: " + c);
        guildUser.getUser().addRole(role);
        ConfigHandler.setSetting(CurrentMoneyConfig.class, guildUser, c - f);
    }
}
