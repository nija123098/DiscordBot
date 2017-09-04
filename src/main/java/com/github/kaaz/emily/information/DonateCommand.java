package com.github.kaaz.emily.information;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.DiscordClient;
import com.github.kaaz.emily.util.FormatHelper;

/**
 * Made by nija123098 on 5/11/2017.
 */
public class DonateCommand extends AbstractCommand {
    public DonateCommand() {
        super("donate", ModuleLevel.INFO, "contribute", null, "Provides general info about how to contribute or donate to Emily");
    }
    @Command
    public void command(MessageMaker maker){
        maker.getTitle().append("You're interested in contributing, that's great!");
        maker.append("**Found a bug!**\n" +
                "You can report them on `!discord`.\n" +
                "\n" +
                "**Want to contribute or share your thoughts?**\n" +
                "Feel free to join `!discord` and let your voice be heard! Feedback and suggestions are always welcome!\n" +
                "\n" +
                "**You'd like to donate?**\n" +
                "You can do this though my " + FormatHelper.embedLink("Patreon", "https://www.patreon.com/emilybot") + ".");
        maker.withThumb(DiscordClient.getOurUser().getAvatarURL());
    }
}
