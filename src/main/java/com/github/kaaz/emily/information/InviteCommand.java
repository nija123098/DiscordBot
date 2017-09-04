package com.github.kaaz.emily.information;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.DiscordClient;
import com.github.kaaz.emily.launcher.BotConfig;
import com.github.kaaz.emily.util.FormatHelper;
import org.apache.http.message.BasicNameValuePair;
import sx.blah.discord.api.internal.DiscordEndpoints;
import sx.blah.discord.api.internal.Requests;
import sx.blah.discord.api.internal.json.responses.GatewayBotResponse;

/**
 * Made by nija123098 on 5/11/2017.
 */
public class InviteCommand extends AbstractCommand {
    private static final int GUILD_ESTIMATE = Requests.GENERAL_REQUESTS.GET.makeRequest(DiscordEndpoints.GATEWAY + "/bot", GatewayBotResponse.class, new BasicNameValuePair("Authorization", "Bot " + BotConfig.BOT_TOKEN), new BasicNameValuePair("Content-Type", "application/json")).shards * 1000;
    public InviteCommand() {
        super("invite", ModuleLevel.INFO, "inv", null, "Provides an invite link to add the bot to your server.");
    }
    @Command
    public void command(MessageMaker maker){
        maker.getTitle().append("I am honored you'd want to invite me! :hugging:");
        maker.append("You can add me to your guild/server with this " + FormatHelper.embedLink("link", "https://discordapp.com/oauth2/authorize?client_id=212834061306036224&scope=bot&permissions=70634560") +
                ".\nI am serving over " + GUILD_ESTIMATE + " servers and counting!")
                .withImage(DiscordClient.getOurUser().getAvatarURL());
    }
}
