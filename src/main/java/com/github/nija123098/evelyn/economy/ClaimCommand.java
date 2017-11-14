package com.github.nija123098.evelyn.economy;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.economy.configs.CurrentMoneyConfig;
import com.github.nija123098.evelyn.economy.configs.LastMoneyUseConfig;
import com.github.nija123098.evelyn.economy.configs.MoneyNameConfig;
import com.github.nija123098.evelyn.economy.configs.MoneySymbolConfig;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

/**
 * Made by nija123098 on 6/6/2017.
 */
public class ClaimCommand extends AbstractCommand {
    public ClaimCommand() {
        super("claim", ModuleLevel.ECONOMY, "dailies", "cookie", "Claim money in the local currency!");
    }
    @Command
    public void command(Guild guild, User user, MessageMaker maker){
        String name = ConfigHandler.getSetting(MoneyNameConfig.class, guild), symbol = ConfigHandler.getSetting(MoneySymbolConfig.class, guild);
        try  {
            Instant.parse(ConfigHandler.getSetting(LastMoneyUseConfig.class, user));
        } catch (DateTimeParseException e) {
            ConfigHandler.setSetting(LastMoneyUseConfig.class, user, "2017-11-10T00:00:00.000Z");
            maker.appendRaw("Hi " + user.getDisplayName(guild) + ", I recently switched to a new claim format! You can now claim the value in full after midnight UTC every day\nYou keep all your current " + name + " and I've set it up to get you a free claim!\n\n");
        }
        int currentMoney = ConfigHandler.getSetting(CurrentMoneyConfig.class, user);
        Instant then = Instant.parse(ConfigHandler.getSetting(LastMoneyUseConfig.class, user)), thenDays = then.truncatedTo(ChronoUnit.DAYS), now = Clock.systemUTC().instant(), nowDays = now.truncatedTo(ChronoUnit.DAYS);
        Instant utcMidnight = Instant.parse(ZonedDateTime.now(ZoneId.of("Z")).plusDays(1).truncatedTo(ChronoUnit.SECONDS).toString());
        int timeUntil = Math.abs(Integer.valueOf(String.valueOf(now.until(utcMidnight.atZone(ZoneId.of("Z")).truncatedTo(ChronoUnit.DAYS), ChronoUnit.MINUTES))));
        int hours = 0, minutes;
        while (timeUntil >= 60) {
            hours++;
            timeUntil = timeUntil - 60;
        }
        minutes = timeUntil;
        if (nowDays.compareTo(thenDays) == 1) {
            maker.appendRaw("You have claimed `\u200b " + symbol + " 256 \u200b`\nYou now have `\u200b " + symbol + " " + (currentMoney + 256) + " \u200b`");
            ConfigHandler.setSetting(CurrentMoneyConfig.class, user, (currentMoney + 256));
            ConfigHandler.setSetting(LastMoneyUseConfig.class, user, now.toString());
        } else {
            maker.appendRaw("You can claim the next batch of " + name + " in " + hours + "h and " + minutes + "m");
        }
    }
}
