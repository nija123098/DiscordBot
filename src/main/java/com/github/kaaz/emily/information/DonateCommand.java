package com.github.kaaz.emily.information;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;

/**
 * Made by nija123098 on 5/11/2017.
 */
public class DonateCommand extends AbstractCommand {
    public DonateCommand() {
        super("donate", ModuleLevel.INFO, "contribute", null, "Provides general info about how to contribute or donate to Emily");
    }
    @Command
    public void command(MessageMaker maker){
        maker.append("You're interested in contributing, that's great!\n" +
                "\n" +
                "Found a bug!\n" +
                "You can report them on either !discord or !github\n" +
                "\n" +
                "Want to contribute or share your thoughts?\n" +
                "Feel free to join !discord and let your voice be heard! Feedback and suggestions are always welcome!\n" +
                "\n" +
                "You know how to speak 0101?\n" +
                "Check out !github and feel free to pick up one of the open issues\n" +
                "\n" +
                "If you've ascended beyond 0101 and know multiple numbers, consider following the project on github to see whats happening\n" +
                "\n" +
                "You'd like to donate?\n" +
                "You can do this though patreon.  https://www.patreon.com/emilybot");
    }
}
