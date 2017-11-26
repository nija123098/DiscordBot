package com.github.nija123098.evelyn.information;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient;
import com.github.nija123098.evelyn.util.FormatHelper;

/**
 * Made by nija123098 on 5/11/2017.
 */
public class InviteCommand extends AbstractCommand {
    public InviteCommand() {
        super("invite", ModuleLevel.INFO, null, null, "Provides an invite link to add the bot to your server.");
    }
    @Command
    public void command(MessageMaker maker){
        maker.getTitle().append("I am honored you'd want to invite me! :hugging:");
        maker.append("You can add me to your guild/server with this " + FormatHelper.embedLink("link", "https://discordapp.com/oauth2/authorize?client_id=355764285387046924&scope=bot&permissions=70634560") +
                ".\nI am serving " + DiscordClient.getGuilds().size() + " servers and counting!")
                .withImage(DiscordClient.getOurUser().getAvatarURL());
    }
}
