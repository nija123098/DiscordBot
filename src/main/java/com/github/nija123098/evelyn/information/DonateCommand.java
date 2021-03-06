package com.github.nija123098.evelyn.information;

import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class DonateCommand extends AbstractCommand {
    public DonateCommand() {
        super("donate", ModuleLevel.INFO, "contribute", null, "Provides general info about how to contribute or donate to Evelyn");
    }
    @Command
    public void command(MessageMaker maker) {
        maker.getTitle().append("Contributions!");
        maker.append("\u200B\n**Bugs?**\n" +
                "Report them ").appendEmbedLink("here", ConfigProvider.URLS.discordInviteUrl()).append(
                "\n" +
                "**Want to contribute or give feedback?**\n" +
                "Feel free to join ").appendEmbedLink("here", ConfigProvider.URLS.discordInviteUrl()).append("\n" +
                "**Want to donate?**\n" +
                "You can do that though ").appendEmbedLink("Patreon", ConfigProvider.URLS.donateUrl()).appendRaw(".");
        maker.withThumb(DiscordClient.getOurUser().getAvatarURL());
    }
}
