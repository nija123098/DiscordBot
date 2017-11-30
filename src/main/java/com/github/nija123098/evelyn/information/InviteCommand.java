package com.github.nija123098.evelyn.information;

import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient;
import com.github.nija123098.evelyn.util.FormatHelper;
import com.github.nija123098.evelyn.util.GeneralEmotes;

/**
 * Made by nija123098 on 5/11/2017.
 */
public class InviteCommand extends AbstractCommand {
    public InviteCommand() {
        super("invite", ModuleLevel.INFO, null, null, "Provides an invite link to add the bot to your server.");
    }
    @Command
    public void command(MessageMaker maker){
        maker.getTitle().append("I am honored you'd want to invite me! " + GeneralEmotes.BLOBAWW);
        maker.append("You can add me to your guild/server with this " + FormatHelper.embedLink("link", ConfigProvider.urls.bot_invite_url()) +
                ".\nI am serving " + FormatHelper.embedLink(String.valueOf(DiscordClient.getGuilds().size()),"") + " servers and counting!")
                .withThumb(DiscordClient.getOurUser().getAvatarURL());
    }
}
