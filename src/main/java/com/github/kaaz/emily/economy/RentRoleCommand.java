package com.github.kaaz.emily.economy;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ContextType;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Argument;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.GuildUser;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.Role;
import com.github.kaaz.emily.economy.configs.*;
import com.github.kaaz.emily.exeption.ArgumentException;
import com.github.kaaz.emily.util.FormatHelper;
import com.github.kaaz.emily.util.Time;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Made by nija123098 on 5/16/2017.
 */
public class RentRoleCommand extends AbstractCommand {
    public RentRoleCommand() {
        super("rentrole", ModuleLevel.ECONOMY, "subscriberole, subrole", null, "Rents a role from a server");
    }
    @Command
    public void command(@Argument(optional = true, replacement = ContextType.NONE) Role role, @Argument Time time, GuildUser guildUser, MessageMaker maker){
        if (role == null){
            String icon = ConfigHandler.getSetting(MoneySymbolConfig.class, guildUser.getGuild());
            List<String> list = guildUser.getGuild().getRoles().stream().map(role1 -> {
                Float price = ConfigHandler.getSetting(RoleRentConfig.class, role1);
                return price == null ? null : role1.getName() + (price != 0 ? " for " + price + "" + icon : "");
            }).filter(Objects::nonNull).collect(Collectors.toList());
            if (list.isEmpty()) maker.append("There are no roles in this guild to get.");
            else {
                maker.append("You may get these roles:");
                maker.appendRaw(FormatHelper.makeTable(list));
            }
        } else {
            int hours = (int) (time.timeUntil() / 3600);
            Float currency = ConfigHandler.getSetting(RoleRentConfig.class, role);
            if (currency == null) throw new ArgumentException("You can't rent this role");
            MoneyTransfer.transact(guildUser, guildUser.getGuild(), 0, currency * hours, "The purchase of the role " + role.getName() + " for " + hours + " hours");
            ConfigHandler.alterSetting(RoleSubscriptionsConfig.class, guildUser, map -> map.compute(role, (r, lon) -> lon == null ? time.schedualed() : time.timeUntil() + lon));
            RoleSubscriptionsConfig.scheduleRemoval(time.timeUntil(), guildUser, role);
        }
    }
}
