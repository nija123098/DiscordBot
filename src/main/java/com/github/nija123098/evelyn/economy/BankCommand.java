package com.github.nija123098.evelyn.economy;

import com.github.nija123098.evelyn.audio.Track;
import com.github.nija123098.evelyn.audio.YoutubeTrack;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.GlobalConfigurable;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.economy.configs.*;
import com.github.nija123098.evelyn.util.EmoticonHelper;

import java.awt.*;
import java.time.*;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Made by nija123098 on 5/1/2017.
 */
public class BankCommand extends AbstractCommand {
    public BankCommand() {
        super("bank", ModuleLevel.ECONOMY, null, null, "For checking your balance and claiming your currency");
    }
    @Command
    public void command(Guild guild, User user, MessageMaker maker){
        String name = ConfigHandler.getSetting(CurrencyNameConfig.class, guild), symbol = ConfigHandler.getSetting(CurrencySymbolConfig.class, guild), booster = EmoticonHelper.getChars("rocket", false), claim = EmoticonHelper.getChars("inbox_tray", false);
        int totalClaim = 256, bonus = 0;
        int streak = ConfigHandler.getSetting(CurrentCurrencyStreakConfig.class, user);
        maker.withColor(new Color(54,57,62));
        /**
         * Check for old money timer in database or if empty
         */
        try {
            Instant.parse(ConfigHandler.getSetting(LastCurrencyUseConfig.class, user));
            if (ConfigHandler.getSetting(LastCurrencyUseConfig.class, user).isEmpty() || ConfigHandler.getSetting(LastCurrencyUseConfig.class, user) == null) throw new IllegalArgumentException();
        } catch (DateTimeParseException |IllegalArgumentException e) {
            ConfigHandler.setSetting(LastCurrencyUseConfig.class, user, "2017-11-10T00:00:00.000Z");
            maker.appendRaw("Hi " + user.getDisplayName(guild) + ", I recently switched to a new currency format! You can now claim the value in full after midnight UTC every day\nYou keep all your current " + name + " and as consolation for all the trouble you get a free claim! (as well as a bonus `\u200b " + symbol + " 1000 \u200b`)\n\n");
            bonus = 1000;
            totalClaim += 1000;
        }

        /**
         * Get most variables and check how many days it's been since last claim
         */
        int currentMoney = ConfigHandler.getSetting(CurrentCurrencyConfig.class, user);
        Instant then = Instant.parse(ConfigHandler.getSetting(LastCurrencyUseConfig.class, user)), thenDays = then.truncatedTo(ChronoUnit.DAYS), thenDaysCount = then.truncatedTo(ChronoUnit.DAYS);
        Instant now = Clock.systemUTC().instant(), nowDays = now.truncatedTo(ChronoUnit.DAYS), nowDaysCount = now.truncatedTo(ChronoUnit.DAYS);
        Instant utcMidnight = Instant.parse((ZonedDateTime.now(ZoneId.of("Z")).plusDays(1)).toString());
        int timeUntil = Math.abs(Integer.valueOf(String.valueOf(now.until(utcMidnight.atZone(ZoneId.of("Z")).truncatedTo(ChronoUnit.DAYS), ChronoUnit.MINUTES))));
        int hours = 0, minutes;
        int counter = -1;
        while (nowDaysCount.compareTo(thenDaysCount) > 0) {
            counter++;
            thenDaysCount = thenDaysCount.plus(Duration.ofDays(1));
        }

        try {
            if (!ConfigHandler.getSetting(EventActiveConfig.class, GlobalConfigurable.GLOBAL) && (Instant.parse(ConfigHandler.getSetting(EventStartConfig.class, GlobalConfigurable.GLOBAL))).equals(nowDays)) {
                ConfigHandler.setSetting(EventActiveConfig.class, GlobalConfigurable.GLOBAL, true);
            } else if (ConfigHandler.getSetting(EventActiveConfig.class, GlobalConfigurable.GLOBAL) && (Instant.parse(ConfigHandler.getSetting(EventEndConfig.class, GlobalConfigurable.GLOBAL))).equals(nowDays)) {
                ConfigHandler.setSetting(EventActiveConfig.class, GlobalConfigurable.GLOBAL, false);
            }
        } catch (DateTimeParseException ignored) {

        }

        /**
         * Calculate if the streak is valid, else remove X days from streak
         */
        if (counter < 0) counter = 0;
        streak = streak - counter;
        if (streak < 0) {
            streak = 0;
        }
        while (timeUntil >= 60) {
            hours++;
            timeUntil = timeUntil - 60;
        }
        totalClaim = totalClaim + (streak * 8) + (ConfigHandler.getSetting(EventActiveConfig.class, GlobalConfigurable.GLOBAL) ? ConfigHandler.getSetting(EventBonusConfig.class, GlobalConfigurable.GLOBAL) : 0);
        minutes = timeUntil;

        /**
         * Checks if it's been a day since the last time the user claimed, this is checked against 00:00 UTC, if it was more recent, display the time until 00:00 UTC
         */
        if (nowDays.compareTo(thenDays) == 1) {
            maker.appendRaw(ClaimBuilder((hours + "h " + minutes + "m"), symbol, bonus, streak, user, guild, true)).mustEmbed();
            ConfigHandler.setSetting(CurrentCurrencyConfig.class, user, (currentMoney + totalClaim));
            ConfigHandler.setSetting(LastCurrencyUseConfig.class, user, now.truncatedTo(ChronoUnit.SECONDS).toString());
            if (streak < 8) ConfigHandler.setSetting(CurrentCurrencyStreakConfig.class, user, (streak + 1));
        } else {
            if (hours == 0 && minutes >= 1) {
                GuildAudioManager manager = GuildAudioManager.getManager(guild);
                if (manager != null) {
                    List<Track> thing = new ArrayList<>();
                    thing.add(manager.currentTrack());
                    thing.addAll(manager.getQueue());
                    manager.clearQueue();
                    manager.queueTrack(new YoutubeTrack("9jK-NcRmVcw"));
                    manager.skipTrack();
                    thing.forEach(manager::queueTrack);
                }
                maker.withImage("https://media.giphy.com/media/iQxHV4HVDJuk8/giphy.gif");
                return;
            }
            maker.appendRaw(ClaimBuilder((hours + "h " + minutes + "m"), symbol, bonus, streak, user, guild, false)).mustEmbed();
        }
    }

    public String ClaimBuilder(String time, String moneySymbol, int bonus, int streak, User user, Guild guild, boolean claim) {
        StringBuilder ret = new StringBuilder("```\n");
        String eventStart = "", eventEnd = "";
        if (ConfigHandler.getSetting(EventActiveConfig.class, GlobalConfigurable.GLOBAL)){
            eventStart = ConfigHandler.getSetting(EventStartConfig.class, GlobalConfigurable.GLOBAL).substring(8, 10) + ConfigHandler.getSetting(EventStartConfig.class, GlobalConfigurable.GLOBAL).substring(4, 7).replace('-', '/');
            eventEnd = ConfigHandler.getSetting(EventEndConfig.class, GlobalConfigurable.GLOBAL).substring(8, 10) + ConfigHandler.getSetting(EventEndConfig.class, GlobalConfigurable.GLOBAL).substring(4, 7).replace('-', '/');
        }
        ret.append("\uD83C\uDFE7 @" + user.getDisplayName(guild) + " \uD83C\uDFE7\n");
        ret.append("════════════════════════════════════════\n");
        if (claim) {
            ret.append(" Claim: " + moneySymbol + " +256\n");
            ret.append(" Daily: " + moneySymbol + " +" + (streak * 8) + " (" + streak + " \uD83D\uDCC6)\n");
            if (bonus > 0) ret.append(" Bonus: " + moneySymbol + " +" + bonus + "\n");
            if (ConfigHandler.getSetting(EventActiveConfig.class, GlobalConfigurable.GLOBAL)) ret.append(" Event: " + moneySymbol + " +" + ConfigHandler.getSetting(EventBonusConfig.class, GlobalConfigurable.GLOBAL) + " [" + ConfigHandler.getSetting(EventNameConfig.class, GlobalConfigurable.GLOBAL) + ", " + eventStart + " to " + eventEnd + "]\n");
        } else {
            ret.append(" Claim: \u23f0 " + time + "\n");
        }
        ret.append("════════════════════════════════════════\n");
        if (claim) {
            ret.append(" Funds: " + moneySymbol + " " + (ConfigHandler.getSetting(CurrentCurrencyConfig.class, user) + bonus + (streak * 8) + 256) + " Daily: \uD83D\uDCC6 " + (streak + (streak == 8 ? 0 : 1)) + "/8\n");
        } else {
            ret.append(" Funds: " + moneySymbol + " " + ConfigHandler.getSetting(CurrentCurrencyConfig.class, user) + " Daily: \uD83D\uDCC6 " + streak + "/8\n");
        }
        ret.append("```");
        return ret.toString();
    }
}
