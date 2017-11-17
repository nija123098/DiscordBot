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
public class DonateCommand extends AbstractCommand {
    public DonateCommand() {
        super("donate", ModuleLevel.INFO, "contribute", null, "Provides general info about how to contribute or donate to Evelyn");
    }
    @Command
    public void command(MessageMaker maker){
        maker.getTitle().append("Contributions!");
        maker.append("**Did you find a bug?**\n" +
                "You can report them on").appendRaw("`!discord`.\n").append(
                "\n" +
                "**Want to contribute or share your thoughts?**\n" +
                "Feel free to join ").appendRaw("`!discord`").append(" and let your voice be heard! Feedback and suggestions are always welcome!\n" +
                "\n" +
                "**You'd like to donate?**  (Yes you would)\n" +
                "You can do this though my ").appendRaw(FormatHelper.embedLink("Patreon", "https://www.patreon.com/soarnija") + ".");
        maker.withThumb(DiscordClient.getOurUser().getAvatarURL());
    }
}
