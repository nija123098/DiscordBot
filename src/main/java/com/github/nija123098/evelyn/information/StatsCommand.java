package com.github.nija123098.evelyn.information;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.Shard;
import com.github.nija123098.evelyn.util.EmoticonHelper;
import com.github.nija123098.evelyn.util.FormatHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class StatsCommand extends AbstractCommand {
    public StatsCommand() {
        super("stats", ModuleLevel.INFO, "guildstats", null, "shows some statistics");
    }
    @Command
    public void command(MessageMaker maker, String s){
        maker.mustEmbed().getTitle().clear().appendRaw(EmoticonHelper.getChars("chart_with_upwards_trend",false) + " Evelyn Stats");
        maker.appendRaw(getTotalTable(s.startsWith("mini")));
    }
    private String getTotalTable(boolean minified) {
        List<List<String>> body = new ArrayList<>();
        int totGuilds = 0, totUsers = DiscordClient.getUsers().size(), totChannels = 0, totVoice = 0, totActiveVoice = 0;
        for (Shard shard : DiscordClient.getShards()) {
            List<Guild> guilds = shard.getGuilds();
            int numGuilds = guilds.size();
            int users = shard.getUsers().size();
            int channels = shard.getChannels().size();
            int voiceChannels = shard.getVoiceChannels().size();
            int activeVoice = 0;
            for (Guild guild : shard.getGuilds()) {
                if (GuildAudioManager.getManager(guild) != null) {
                    activeVoice++;
                }
            }
            totGuilds += numGuilds;
            totChannels += channels;
            totVoice += voiceChannels;
            totActiveVoice += activeVoice;
            if (minified) body.add(Arrays.asList("" + numGuilds, "" + users, "" + channels, "" + voiceChannels, "" + activeVoice));
            else body.add(Arrays.asList("" + shard.getID(), "" + numGuilds, "" + users, "" + channels, "" + voiceChannels, "" + activeVoice));
        }
        List<String> header = minified ? Arrays.asList("G", "U", "T", "V", "DJ") : Arrays.asList("Shard", "Guilds", "Users", "Text", "Voice", "DJ");
        if (DiscordClient.getShards().size() > 1) {
            if (minified) {
                return FormatHelper.makeAsciiTable(header, body, Arrays.asList("" + totGuilds, "" + totUsers, "" + totChannels, "" + totVoice, "" + totActiveVoice));
            }
            return FormatHelper.makeAsciiTable(header, body, Arrays.asList("TOTAL", "" + totGuilds, "" + totUsers, "" + totChannels, "" + totVoice, "" + totActiveVoice));
        }
        return FormatHelper.makeAsciiTable(header, body, null);
    }
}
