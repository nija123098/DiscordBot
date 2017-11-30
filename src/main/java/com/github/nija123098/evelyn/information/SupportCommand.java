package com.github.nija123098.evelyn.information;

import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient;
import com.github.nija123098.evelyn.util.EmoticonHelper;
import com.github.nija123098.evelyn.util.FormatHelper;

/**
 * Made by nija123098 on 5/30/2017.
 */
public class SupportCommand extends AbstractCommand {
    public SupportCommand() {
        super("support", ModuleLevel.INFO, "discord", null, "Brings you to my support server");
    }
    @Command
    public void command(MessageMaker maker){
        maker.mustEmbed().withThumb(DiscordClient.getOurUser().getAvatarURL());
        maker.getTitle().clear().appendRaw(EmoticonHelper.getChars("tools",false) + " Evelyn Support");
        maker.appendRaw("If you need help or have any questions/suggestions\nfeel free to join the Evelyn discord " + FormatHelper.embedLink("here", ConfigProvider.urls.discord_invite_url()) + ".");
    }
}
