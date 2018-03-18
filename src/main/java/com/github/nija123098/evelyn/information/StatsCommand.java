package com.github.nija123098.evelyn.information;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.GlobalConfigurable;
import com.github.nija123098.evelyn.config.configs.guild.GuildIgnoreConfig;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.Shard;
import com.github.nija123098.evelyn.util.EmoticonHelper;
import com.github.nija123098.evelyn.util.FormatHelper;

import java.util.*;
import java.util.function.Function;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class StatsCommand extends AbstractCommand {
    public StatsCommand() {
        super("stats", ModuleLevel.DEVELOPMENT, "guildstats", null, "Shows statistics about the overall bot");
    }

    @Command
    public void command(MessageMaker maker, String s) {
        maker.mustEmbed().getTitle().clear().appendRaw(EmoticonHelper.getChars("chart_with_upwards_trend",false) + " Evelyn Stats");
        maker.appendRaw(getTotalTable(s.startsWith("mini")));
    }

    static String getTotalTable(boolean mini) {
        List<List<String>> body = new ArrayList<>();
        List<Guild> ignored = ConfigHandler.getSetting(GuildIgnoreConfig.class, GlobalConfigurable.GLOBAL);
        int totalActiveVoice = 0, activeVoice;
        for (Shard shard : DiscordClient.getShards()) {
            List<Guild> guilds = shard.getGuilds();
            guilds.removeAll(ignored);
            activeVoice = (int) shard.getGuilds().stream().map(GuildAudioManager::getManager).filter(Objects::nonNull).count();
            totalActiveVoice += activeVoice;
            body.add(Arrays.asList(Integer.toString(shard.getID()), Integer.toString(guilds.size()), Integer.toString(count(guilds, Guild::getUsers)), Integer.toString(countUnique(guilds, Guild::getChannels)), Integer.toString(countUnique(guilds, Guild::getVoiceChannels)), Integer.toString(activeVoice)));
        }
        return FormatHelper.makeAsciiTable(mini ? Arrays.asList("G", "U", "T", "V", "DJ") : Arrays.asList("Shard", "Guilds", "Users", "Text", "Voice", "DJ"), body, DiscordClient.getShards().size() == 1 ? null : Arrays.asList("TOTAL", Integer.toString(DiscordClient.getGuilds().size()), Integer.toString(DiscordClient.getUsers().size()), Integer.toString(DiscordClient.getChannels().size()), Integer.toString(DiscordClient.getVoiceChannels().size()), Integer.toString(totalActiveVoice)));
    }
    private static <E> int count(List<E> es, Function<E, List<?>> function) {
        return es.stream().map(function).collect(HashSet::new, AbstractCollection::addAll, AbstractCollection::addAll).size();
    }
    private static <E> int countUnique(List<E> es, Function<E, List<?>> function) {
        return es.stream().map(e -> function.apply(e).size()).reduce((integer, integer2) -> integer + integer2).orElse(0);
    }
}
