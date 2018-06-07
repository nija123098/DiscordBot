package com.github.nija123098.evelyn.information;

import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.CommandHandler;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.configs.guild.GuildPrefixConfig;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.tag.Tag;
import com.github.nija123098.evelyn.tag.Tags;
import com.github.nija123098.evelyn.util.EmoticonHelper;

/**
 * @author nija123098
 * @since 1.0.0
 */
@Tags(value = {Tag.HELPFUL})
public class InfoCommand extends AbstractCommand {
    private static int totalCommands = -1;
    public InfoCommand() {
        super("info", ModuleLevel.INFO, "about, information", null, "Gives some information about me");
    }
    @Command
    public void command(MessageMaker maker, Guild guild) {
        if (totalCommands == -1) totalCommands = (int) CommandHandler.getCommands().stream().filter(AbstractCommand::isHighCommand).filter(o -> !o.isTemplateCommand()).count();
        maker.withThumb(DiscordClient.getOurUser().getAvatarURL());
        maker.getTitle().clear().appendRaw(EmoticonHelper.getChars("bulb", false) + "INFO");
        maker.appendRaw("\u200B\n").append("I am Evelyn, a music playing auto moderation bot!\n" +
                "\n" +
                "Type ").appendEmbedLink("@Evelyn help", "").append(" to see a list of commands. In total there are ").appendEmbedLink(String.valueOf(totalCommands),"").append(" unique commands I can perform.\n" +
                "\n" +
                "For help about a command type ").appendEmbedLink(ConfigHandler.getSetting(GuildPrefixConfig.class, guild) + "help <command>\n", "").append(
                "For example: `").appendRaw(ConfigHandler.getSetting(GuildPrefixConfig.class, guild) + "help ping`").append(" to see what you can do with the ping command.\n" +
                "\n" +
                "If you want assistance, to share your thoughts, or to contribute, you should join my Discord server ").appendEmbedLink("here", ConfigProvider.URLS.discordInviteUrl()).appendRaw(".");
    }
}
