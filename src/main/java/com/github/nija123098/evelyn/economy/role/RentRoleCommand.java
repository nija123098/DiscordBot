package com.github.nija123098.evelyn.economy.role;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ContextType;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Role;
import com.github.nija123098.evelyn.economy.configs.CurrencySymbolConfig;
import com.github.nija123098.evelyn.economy.currencytransfer.CurrencyTransfer;
import com.github.nija123098.evelyn.economy.role.configs.RoleRentConfig;
import com.github.nija123098.evelyn.economy.role.configs.RoleSubscriptionsConfig;
import com.github.nija123098.evelyn.exception.ArgumentException;
import com.github.nija123098.evelyn.util.FormatHelper;
import com.github.nija123098.evelyn.util.Time;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class RentRoleCommand extends AbstractCommand {
    public RentRoleCommand() {
        super("rentrole", ModuleLevel.ECONOMY, "subscriberole, subrole", null, "Rents a role from a server");
    }
    @Command
    public void command(@Argument(optional = true, replacement = ContextType.NONE) Role role, @Argument Time time, GuildUser guildUser, MessageMaker maker) {
        if (role == null) {
            String icon = ConfigHandler.getSetting(CurrencySymbolConfig.class, guildUser.getGuild());
            List<String> list = guildUser.getGuild().getRoles().stream().map(role1 -> {
                Integer price = ConfigHandler.getSetting(RoleRentConfig.class, role1);
                return price == null ? null : role1.getName() + (price != 0 ? " for " + price + "" + icon : "");
            }).filter(Objects::nonNull).collect(Collectors.toList());
            if (list.isEmpty()) maker.append("There are no roles in this guild to get.");
            else {
                maker.append("You may get these roles:");
                maker.appendRaw(FormatHelper.makeTable(list));
            }
        } else {
            int hours = (int) (time.timeUntil() / 3600);
            Integer currency = ConfigHandler.getSetting(RoleRentConfig.class, role);
            if (currency == null) throw new ArgumentException("You can't rent this role");
            CurrencyTransfer.transact(guildUser, guildUser.getGuild(), 0, currency * hours, "The purchase of the role " + role.getName() + " for " + hours + " hours");
            ConfigHandler.alterSetting(RoleSubscriptionsConfig.class, guildUser, map -> map.compute(role, (r, lon) -> lon == null ? time.schedualed() : time.timeUntil() + lon));
            RoleSubscriptionsConfig.scheduleRemoval(time.timeUntil(), guildUser, role);
        }
    }
}
