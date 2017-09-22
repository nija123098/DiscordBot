package com.github.nija123098.evelyn.information;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.util.EmoticonHelper;

import java.util.ArrayList;

/**
 * Written by Soarnir 12/9/17
 */

public class GuideCommand extends AbstractCommand {
    public GuideCommand() {
        super("guide", ModuleLevel.INFO, null, null, "A quickstart guide to Evelyn");
    }

    @Command
    public void command(MessageMaker maker) {
        final String GuideTextZero = "Hi! My name is Evelyn\nI'm a multipurpose bot with hundreds of features. This is a short guide to help you understand how I work!";
        final String guideTextOne = "\n**You can edit commands:**\nIf you make a typo in a command, and I react to it with a " + EmoticonHelper.getChars("grey_question", false) + "\nsimply edit your command and it will work!\n" +
                "\n**I like reactions:**\nA lot of my commands/features use reactions, for example this guide uses reactions to switch pages. Some of my commands can only be activated by reactions: ex. star board, slots, translate.\n" +
                "Other commands can use reactions as well as the usual text, you'll find the appropriate use when you do `!help <command>`\n" +
                "\n**Configuration:**\nJust about any and all features of mine are configurable, including channels, roles, users, just try `!config` with any of the previous parameters and see what happens.";
        final String guideTextTwo = "\n**Cookies are the universal currency:**\nThis is fact, don't question it.\n" +
                "\n**Cookies:**\nCookies are stored in two ways, your personal bank, and your guild bank.\n" +
                "Your guild bank cookies can only be used in that guild, for purchasing roles, etc.\n" +
                "Your personal bank of cookies don't decrease when you use your guild bank cookies, but are instead used for other features you might see Soon" + EmoticonHelper.getChars("tm",false) + "\n";
        final String guideTextThree = "\n**DM commands:**\nI can recognise all commands in direct message with me, and you'll need to use `!cfg` there to set all your user configurations, like your spoken language.\n" +
                "\n**Translation:**\nI have a translation functionality! Once you've set your user language as I said in my previous point, you can react to any message with " +
                EmoticonHelper.getChars("speech_balloon", false) + " or " + EmoticonHelper.getChars("speech_left", false) + " and I'll do my best to translate that message for you!\n" +
                "\n**Calls:**\nDid I mention you can open up a call to another server? Well, you can! `!help call` is all you need.\n" +
                "**Reputation:**\nDo you think someone said something nice? Something awesome? Did it inspire you? Well now you can show your happiness by giving someone a reputation point. Simply react to a message they sent with " + EmoticonHelper.getChars("thumbsup", false) +
                "\n\n**Star Board:**\nRather than give someone just a reputation point, why not try to feature their message on your server's star board? React with enough stars to someone's message and I'll feature it in the star board channel! The more stars, the fancier the message!\n";
        final String guideTextFour = "";
        ArrayList guideText = new ArrayList();
        guideText.add(GuideTextZero);
        guideText.add(guideTextOne);
        guideText.add(guideTextTwo);
        guideText.add(guideTextThree);
        guideText.add(guideTextFour);

        maker.getTitle().append("A beginner's guide to me");
        guideText.forEach(page -> maker.getNewListPart().appendRaw(page.toString()));
    }
}