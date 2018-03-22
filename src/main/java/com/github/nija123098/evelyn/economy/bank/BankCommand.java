package com.github.nija123098.evelyn.economy.bank;

import com.github.nija123098.evelyn.audio.Track;
import com.github.nija123098.evelyn.audio.YoutubeTrack;
import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
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
import com.github.nija123098.evelyn.economy.event.configs.*;
import com.github.nija123098.evelyn.tag.Tag;
import com.github.nija123098.evelyn.tag.Tags;
import com.github.nija123098.evelyn.util.EmoticonHelper;
import com.github.nija123098.evelyn.util.Time;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Soarnir
 * @since 1.0.0
 */
@Tags(value = {Tag.ARCADE})
public class BankCommand extends AbstractCommand {
    public BankCommand() {
        super("bank", ModuleLevel.ECONOMY, "cookie, cookies", "cookie", "For checking your balance and claiming your currency");
    }

    @Command
    public void command(Guild guild, User user, MessageMaker maker) {
        String name = ConfigHandler.getSetting(CurrencyNameConfig.class, guild), symbol = ConfigHandler.getSetting(CurrencySymbolConfig.class, guild), booster = EmoticonHelper.getChars("rocket", false), claim = EmoticonHelper.getChars("inbox_tray", false);
        int totalClaim = 256, bonus = 0;
        /*
         * Check for old money timer in database or if empty
         */
        if (ConfigHandler.getSetting(LastCurrencyUseConfig.class, user).equals(0L) || ConfigHandler.getSetting(LastCurrencyUseConfig.class, user) == null) {
            //ConfigHandler.setSetting(LastCurrencyUseConfig.class, user, System.currentTimeMillis());
            maker.appendRaw("Hi " + user.getDisplayName(guild) + ", I recently switched to a new currency format! You can now claim the value in full after midnight UTC every day\nYou keep all your current " + name + " and as consolation for all the trouble you get a bonus `\u200b " + symbol + " 1000 \u200b`\n\n");
            bonus = ConfigHandler.getSetting(CurrencyBonusConfig.class, GlobalConfigurable.GLOBAL);
        }

        /*
         * Get most variables and check how many days it's been since last claim
         */
        int streak = ConfigHandler.getSetting(CurrentCurrencyStreakConfig.class, user);
        int currentMoney = ConfigHandler.getSetting(CurrentCurrencyConfig.class, user);
        long day = 86400000L;
        long currentMillis = System.currentTimeMillis();
        long last = ConfigHandler.getSetting(LastCurrencyUseConfig.class, user);
        long daysSinceEpoch = currentMillis / day;
        int daysSinceStreak = (int) (daysSinceEpoch - (last / day)) - 1;

        /*
         * TODO: Handle event checking
         */
        /*if (!ConfigHandler.getSetting(EventActiveConfig.class, GlobalConfigurable.GLOBAL)) {
            ConfigHandler.setSetting(EventActiveConfig.class, GlobalConfigurable.GLOBAL, true);
        } else if (ConfigHandler.getSetting(EventActiveConfig.class, GlobalConfigurable.GLOBAL)) {
            ConfigHandler.setSetting(EventActiveConfig.class, GlobalConfigurable.GLOBAL, false);
        }*/

        /*
         * Calculate if the streak is valid, else remove X days from streak
         */
        if (daysSinceStreak < 0) daysSinceStreak = 0;
        streak = streak - daysSinceStreak;
        if (streak < 0) {
            streak = 0;
        }
        totalClaim = totalClaim + (streak * 8) + (ConfigHandler.getSetting(EventActiveConfig.class, GlobalConfigurable.GLOBAL) ? ConfigHandler.getSetting(EventBonusConfig.class, GlobalConfigurable.GLOBAL) : 0);

        /*
         * Check if user is able to claim their cookies
         */
        if ((currentMillis / day) != (last / day)) {
            maker.appendRaw(claimBuilder((Time.getAbbreviated(day - (currentMillis % day))), symbol, bonus, streak, user, guild, true)).mustEmbed();
            ConfigHandler.setSetting(CurrentCurrencyConfig.class, user, (currentMoney + totalClaim + bonus));
            ConfigHandler.setSetting(LastCurrencyUseConfig.class, user, currentMillis);
            if (streak < 8) ConfigHandler.setSetting(CurrentCurrencyStreakConfig.class, user, (streak + 1));
        } else {
            if ((day - (currentMillis % day)) < 60000L) {
                GuildAudioManager manager = GuildAudioManager.getManager(guild);
                if (manager != null) {
                    List<Track> previousTracks = new ArrayList<>();
                    previousTracks.add(manager.currentTrack());
                    previousTracks.addAll(manager.getQueue());
                    manager.clearQueue();
                    manager.queueTrack(new YoutubeTrack("9jK-NcRmVcw"));
                    manager.skipTrack();
                    previousTracks.forEach(manager::queueTrack);
                }
                maker.withImage(ConfigProvider.URLS.bankGif());
                return;
            }
            maker.appendRaw(claimBuilder((Time.getAbbreviated(day - (currentMillis % day))), symbol, bonus, streak, user, guild, false)).mustEmbed();
        }
    }

    private String claimBuilder(String time, String moneySymbol, int bonus, int streak, User user, Guild guild, boolean claim) {
        StringBuilder ret = new StringBuilder("```\n");
        Long eventStart = 0L, eventEnd = 0L;
        if (ConfigHandler.getSetting(EventActiveConfig.class, GlobalConfigurable.GLOBAL)) {
            eventStart = ConfigHandler.getSetting(EventStartConfig.class, GlobalConfigurable.GLOBAL);
            eventEnd = ConfigHandler.getSetting(EventEndConfig.class, GlobalConfigurable.GLOBAL);
        }
        ret.append("\uD83C\uDFE7 @" + user.getDisplayName(guild) + " \uD83C\uDFE7\n");
        ret.append("════════════════════════════════════════\n");
        if (claim) {
            ret.append(" Claim: " + moneySymbol + " +256\n");
            ret.append(" Heat:  " + moneySymbol + " +" + (streak * 8) + " (" + streak + " \uD83D\uDCC6)\n");
            if (bonus > 0) ret.append(" Bonus: " + moneySymbol + " +" + bonus + "\n");
            if (ConfigHandler.getSetting(EventActiveConfig.class, GlobalConfigurable.GLOBAL)) ret.append(" Event: " + moneySymbol + " +" + ConfigHandler.getSetting(EventBonusConfig.class, GlobalConfigurable.GLOBAL) + " [" + ConfigHandler.getSetting(EventNameConfig.class, GlobalConfigurable.GLOBAL) + ", " + eventStart + " to " + eventEnd + "]\n");
        } else {
            ret.append(" Claim: \u23f0 " + time + "\n");
        }
        ret.append("════════════════════════════════════════\n");
        if (claim) {
            ret.append(" Funds: " + moneySymbol + " " + (ConfigHandler.getSetting(CurrentCurrencyConfig.class, user) + bonus + (streak * 8) + 256) + "  Heat:  \uD83D\uDCC6 " + (streak + (streak == 8 ? 0 : 1)) + "/8\n");
        } else {
            ret.append(" Funds: " + moneySymbol + " " + ConfigHandler.getSetting(CurrentCurrencyConfig.class, user) + "  Heat:  \uD83D\uDCC6 " + streak + "/8\n");
        }
        ret.append("```");
        return ret.toString();
    }
}
