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
import com.github.nija123098.evelyn.util.EmoticonHelper;
import com.github.nija123098.evelyn.util.FormatHelper;

import java.awt.*;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class InfoCommand extends AbstractCommand {
    private static int totalCommands = -1;
    public InfoCommand() {
        super("info", ModuleLevel.INFO, "about, information", null, "Gives some information about me");
    }
    @Command
    public void command(MessageMaker maker, Guild guild){
        if (totalCommands == -1) totalCommands = (int) CommandHandler.getCommands().stream().filter(AbstractCommand::isHighCommand).filter(o -> !o.isTemplateCommand()).count();
        maker.mustEmbed().withColor(new Color(39, 209, 110)).withThumb(DiscordClient.getOurUser().getAvatarURL());
        maker.getTitle().clear().appendRaw(EmoticonHelper.getChars("bulb",false) + "INFO");
        String prefix = ConfigHandler.getSetting(GuildPrefixConfig.class, guild);
        maker.appendRaw("\u200B\nI am Evelyn, a music playing auto moderation bot!\n" +
                "\n" +
                "Type " + FormatHelper.embedLink("@Evelyn help","") + " to see a list of commands. In total there are " + FormatHelper.embedLink(String.valueOf(totalCommands),"") + " unique commands I can perform.\n" +
                "\n" +
                "For help about a command type " + FormatHelper.embedLink(prefix + "help <command>\n","") +
                "An example: " + FormatHelper.embedLink("@Evelyn help ping","") + " to see what you can do with the ping command.\n" +
                "\n" +
                "If you need assistance, want to share your thoughts or want to contribute feel free to join my discord " + FormatHelper.embedLink("here", ConfigProvider.URLS.discord_invite_url()) + ".");
    }
}
