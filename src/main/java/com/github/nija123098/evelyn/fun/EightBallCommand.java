package com.github.nija123098.evelyn.fun;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.util.EmoticonHelper;
import com.github.nija123098.evelyn.util.Rand;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class EightBallCommand extends AbstractCommand {
    private static final String EIGHT_BALL = EmoticonHelper.getChars("crystal_ball", false) + " ";
    private static final String[] OPTIONS = {
            "For now, yes.",
            "Maybe yes, maybe no.",
            "Do you really believe that's possible? *sighs*",
            "I don't think you want to know.",
            "I am not in the office right now, try again later.",
            "You can most certainly try ~~and fail~~.",
            "Don't bet on it.",
            "Well, if you think so.",
            "Most certainly impossible",
            "The odds aren't looking favourable.",
            "Only in your dreams.",
            "Certainly.",
            "Do bet on it, just know you will lose",
            "More than likely.",
            "I'm going to say no.",
            "I'm going to say yes.",
            "I can't reach the physic phone line right now.",
            "Will a no satisfy your question?",
            "You've already wasted the opportunity.",
            "I'm going to make stuff up later, and just say yes now.",
            "Sure, sure, go away now.",
            "Very unlikely, super unlikely, as in no chance.",
            "When life gives you lemons, don't make lemonade. Make life take the lemons back, make life rue the day it dared to give you lemons.",
            "Without a doubt.",
            "Are you doubting this? It would seem like you are, yep, definitely doubtful about this one.",
            "Do you find memes funny?",
            "In a manner of speaking yes.",
            "Yes, inject those memes into your blood stream.",
            "You are out of time.",
            "Inconceivable.",
            "Literally impossible.",
            "Ultimately, without going off on a tangent or otherwise, maybe.",
            "If it helps you sleep at night, no."
    };

    public EightBallCommand() {
        super("8ball", ModuleLevel.FUN, "eightball", "crystal_ball", "See what the mystical 8ball has to say");
    }

    @Command
    public void command(MessageMaker maker) {
        maker.appendRaw(EIGHT_BALL).append(OPTIONS[Rand.getRand(OPTIONS.length)]);
    }

    @Override
    protected String getLocalUsages() {
        return "#  8ball <question> // predict the outcome of your yes/no question with the mystical 8ball";
    }
}
