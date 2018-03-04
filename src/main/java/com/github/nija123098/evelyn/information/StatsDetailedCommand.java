package com.github.nija123098.evelyn.information;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.configs.guild.GuildLastCommandTimeConfig;
import com.github.nija123098.evelyn.config.configs.user.LastCommandTimeConfig;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient;
import com.github.nija123098.evelyn.perms.BotRole;
import com.github.nija123098.evelyn.util.EmoticonHelper;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class StatsDetailedCommand extends AbstractCommand {

    public StatsDetailedCommand() {
        super(StatsCommand.class, "detailed", "advanced, more", null, null, "view detailed stats");
    }

    @Command
    public void command(MessageMaker maker, String s){
        maker.mustEmbed().getTitle().clear().appendRaw(EmoticonHelper.getChars("chart_with_upwards_trend",false) + " Evelyn Stats");
        maker.appendRaw(StatsCommand.getTotalTable(s.startsWith("mini")));
        long aMonthAgo = System.currentTimeMillis() - 2_592_000_000L, aWeekAgo = System.currentTimeMillis() - 604_800_000, oneDayAgo = System.currentTimeMillis() - 86_400_000;
        maker.appendRaw("\nActive Guilds: " + DiscordClient.getGuilds().stream().map(GuildLastCommandTimeConfig::get).filter(aLong -> aLong > aWeekAgo).count());
        maker.appendRaw("  Recent users: " + DiscordClient.getUsers().stream().map(LastCommandTimeConfig::get).filter(aLong -> aLong > oneDayAgo).count());
        maker.appendRaw("  Active users: " + DiscordClient.getUsers().stream().map(LastCommandTimeConfig::get).filter(aLong -> aLong > aWeekAgo).count());
        maker.appendRaw("  Monthly users: " + DiscordClient.getUsers().stream().map(LastCommandTimeConfig::get).filter(aLong -> aLong > aMonthAgo).count());
    }

    @Override
    public BotRole getBotRole() {
        return BotRole.CONTRIBUTOR;
    }
}