package com.github.nija123098.evelyn.economy;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ContextType;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Role;
import com.github.nija123098.evelyn.economy.configs.CurrentMoneyConfig;
import com.github.nija123098.evelyn.economy.configs.MoneySymbolConfig;
import com.github.nija123098.evelyn.economy.configs.RoleBuyConfig;
import com.github.nija123098.evelyn.exeption.ArgumentException;
import com.github.nija123098.evelyn.util.FormatHelper;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Made by nija123098 on 5/5/2017.
 */
public class GetRoleCommand extends AbstractCommand {
    public GetRoleCommand() {
        super("getrole", ModuleLevel.ECONOMY, "buyrole, roleme", null, "Allows the buying of roles with guild based money.");
    }
    @Command
    public void command(@Argument(optional = true, replacement = ContextType.NONE) Role role, GuildUser guildUser, MessageMaker maker){
        if (role == null) {
            String icon = ConfigHandler.getSetting(MoneySymbolConfig.class, guildUser.getGuild());
            List<String> list = guildUser.getGuild().getRoles().stream().map(role1 -> {
                Integer price = ConfigHandler.getSetting(RoleBuyConfig.class, role1);
                return price == null ? null : role1.getName() + (price != 0 ? " for " + price + "" + icon : "");
            }).filter(Objects::nonNull).collect(Collectors.toList());
            if (list.isEmpty()) maker.append("There are no roles in this guild to get.");
            else {
                maker.append("You may get these roles:");
                maker.appendRaw(FormatHelper.makeTable(list));
            }
        } else {
            Integer f = ConfigHandler.getSetting(RoleBuyConfig.class, role);
            if (f == null) throw new ArgumentException("You can not buy that role");
            Integer c = ConfigHandler.getSetting(CurrentMoneyConfig.class, guildUser);
            if (c < f) throw new ArgumentException("You must have " + f + " currency to buy that role.  Current: " + c);
            guildUser.getUser().addRole(role);
            ConfigHandler.setSetting(CurrentMoneyConfig.class, guildUser, c - f);
        }
    }
}
