package com.github.kaaz.emily.economy;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.anotations.Argument;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.GuildUser;
import com.github.kaaz.emily.discordobjects.wrappers.Role;
import com.github.kaaz.emily.economy.configs.CurrentMoneyConfig;
import com.github.kaaz.emily.economy.configs.RoleSubscriptionsConfig;
import com.github.kaaz.emily.util.Time;

/**
 * Made by nija123098 on 5/16/2017.
 */
public class RentRoleCommand extends AbstractCommand {
    public RentRoleCommand() {
        super("rentrole", ModuleLevel.ECONOMY, "subscriberole, subrole", null, "Rents a role from a server");
    }
    @Command
    public void command(@Argument Role role, @Argument Time time, GuildUser guildUser){
        int hours = (int) (time.timeUntil() / 3600);
        Float currency = ConfigHandler.getSetting(CurrentMoneyConfig.class, guildUser);
        MoneyTransfer.transact(guildUser, guildUser.getGuild(), -currency * hours, 0, "The purchase of the role " + role.getName() + " for " + hours + " hours");
        ConfigHandler.alterSetting(RoleSubscriptionsConfig.class, guildUser, map -> map.compute(role, (r, lon) -> lon == null ? time.schedualed() : time.timeUntil() + lon));
        RoleSubscriptionsConfig.scheduleRemoval(time.timeUntil(), guildUser, role);
    }
}
