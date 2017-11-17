package com.github.nija123098.evelyn.economy;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.economy.configs.*;
import com.github.nija123098.evelyn.util.EmoticonHelper;

import java.time.*;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

/**
 * Made by nija123098 on 6/6/2017.
 */
public class ClaimCommand extends AbstractCommand {
    public ClaimCommand() {
        super("claim", ModuleLevel.ECONOMY, "dailies", "cookie", "Claim money in the local currency!");
        this.okOnSuccess = false;
    }

    @Command
    public void command(@Argument String arg, Guild guild, User user, MessageMaker maker){
        if (arg.isEmpty()) {
            String name = ConfigHandler.getSetting(MoneyNameConfig.class, guild), symbol = ConfigHandler.getSetting(MoneySymbolConfig.class, guild), booster = EmoticonHelper.getChars("rocket", false);
            int totalClaim = 256;
            int streak = ConfigHandler.getSetting(CurrentMoneyStreakConfig.class, user);

            /**
             * Check for old money timer in database or if empty
             */
            try {
                Instant.parse(ConfigHandler.getSetting(LastMoneyUseConfig.class, user));
                if (ConfigHandler.getSetting(LastMoneyUseConfig.class, user).isEmpty()) throw new IllegalArgumentException();
            } catch (DateTimeParseException|IllegalArgumentException e) {
                ConfigHandler.setSetting(LastMoneyUseConfig.class, user, "2017-11-10T00:00:00.000Z");
                maker.appendRaw("Hi " + user.getDisplayName(guild) + ", I recently switched to a new claim format! You can now claim the value in full after midnight UTC every day\nYou keep all your current " + name + " and as consolation for all the trouble I've set it up to get you a free claim! (as well as a bonus `\u200b " + symbol + " 1000 \u200b`)\n\n");
                totalClaim += 1000;
            }

            /**
             * Get most variables and check how many days it's been since last claim
             */
            int currentMoney = ConfigHandler.getSetting(CurrentMoneyConfig.class, user);
            Instant then = Instant.parse(ConfigHandler.getSetting(LastMoneyUseConfig.class, user)), thenDays = then.truncatedTo(ChronoUnit.DAYS), thenDaysCount = then.truncatedTo(ChronoUnit.DAYS);
            Instant now = Clock.systemUTC().instant(), nowDays = now.truncatedTo(ChronoUnit.DAYS), nowDaysCount = now.truncatedTo(ChronoUnit.DAYS);
            Instant utcMidnight = Instant.parse((ZonedDateTime.now(ZoneId.of("Z")).plusDays(1)).toString());
            int timeUntil = Math.abs(Integer.valueOf(String.valueOf(now.until(utcMidnight.atZone(ZoneId.of("Z")).truncatedTo(ChronoUnit.DAYS), ChronoUnit.MINUTES))));
            int hours = 0, minutes;
            int counter = -1;
            while (nowDaysCount.compareTo(thenDaysCount) > 0) {
                counter++;
                thenDaysCount = thenDaysCount.plus(Duration.ofDays(1));
            }

            /**
             * Calculate if the streak is valid, else remove X days from streak
             */
            streak = streak - counter;
            if (streak <= 0) {
                streak = 1;
            }
            while (timeUntil >= 60) {
                hours++;
                timeUntil = timeUntil - 60;
            }
            totalClaim = totalClaim + (streak * 8);
            minutes = timeUntil;

            /**
             * Calculate spacing for claim display, totalClaimMagnitude is pretty much 4 all the time, but keeping it this way in case bonuses get added later
             */
            int totalClaimMagnitude = String.valueOf(totalClaim).length() + 1;
            int currentMoneyMagnitude = String.valueOf(currentMoney + totalClaim).length();
            String[] spaces = new String[totalClaimMagnitude > currentMoneyMagnitude ? totalClaimMagnitude + 1 : currentMoneyMagnitude + 1];
            String space = "";
            for (int l = 0; l <= (totalClaimMagnitude > currentMoneyMagnitude ? totalClaimMagnitude : currentMoneyMagnitude); l++) {
                spaces[l] = space;
                space = space + " ";
            }

            /**
             * Checks if it's been a day since the last time the user claimed, this is checked against 00:00 UTC, if it was more recent, display the time until 00:00 UTC
             */
            if (nowDays.compareTo(thenDays) == 1) {
                maker.appendRaw("`\u200b Claim: " + symbol + " +" + totalClaim + (streak != 8 ? (spaces[totalClaimMagnitude > currentMoneyMagnitude ? 2 : (2 + (currentMoneyMagnitude - totalClaimMagnitude))] + booster + " +1") : " ") + " \u200b`\n" +
                                "`\u200b Total: " + symbol + " " + (currentMoney + totalClaim) + spaces[currentMoneyMagnitude > totalClaimMagnitude ? 2 : (2 + (totalClaimMagnitude - currentMoneyMagnitude))] + booster + " " + streak + "  \u200b`");//.mustEmbed();
                ConfigHandler.setSetting(CurrentMoneyConfig.class, user, (currentMoney + totalClaim));
                ConfigHandler.setSetting(LastMoneyUseConfig.class, user, now.truncatedTo(ChronoUnit.SECONDS).toString());
                if (streak < 8) ConfigHandler.setSetting(CurrentMoneyStreakConfig.class, user, (streak + 1));
            } else {
                maker.appendRaw("You can claim the next batch of " + name + " in " + hours + "h and " + minutes + "m");
            }
        }
    }
}
