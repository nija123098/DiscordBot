package com.github.kaaz.emily.information;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.github.kaaz.emily.discordobjects.wrappers.DiscordClient;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.Shard;
import com.github.kaaz.emily.util.FormatHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Made by nija123098 on 5/30/2017.
 */
public class StatsCommand extends AbstractCommand {
    public StatsCommand() {
        super("stats", ModuleLevel.INFO, "guildstats", null, "shows some statistics");
    }
    @Command
    public void command(MessageMaker maker, String s){
        maker.appendRaw(getTotalTable(s.startsWith("mini")));
    }
    private String getTotalTable(boolean minified) {
        List<List<String>> body = new ArrayList<>();
        int totGuilds = 0, totUsers = 0, totChannels = 0, totVoice = 0, totActiveVoice = 0;
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
            totUsers += users;
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
