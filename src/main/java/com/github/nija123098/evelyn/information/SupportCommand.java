package com.github.nija123098.evelyn.information;

import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient;
import com.github.nija123098.evelyn.util.EmoticonHelper;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class SupportCommand extends AbstractCommand {
    public SupportCommand() {
        super("support", ModuleLevel.INFO, "discord", null, "Brings you to my support server");
    }
    @Command
    public void command(MessageMaker maker) {
        maker.withThumb(DiscordClient.getOurUser().getAvatarURL());
        maker.getTitle().clear().appendRaw(EmoticonHelper.getChars("tools",false) + " Evelyn Support");
        maker.appendRaw("If you want assistance or have questions or suggestions please join the Evelyn Discord server ").appendEmbedLink("here", ConfigProvider.URLS.discordInviteUrl()).appendRaw(".");
    }
}
