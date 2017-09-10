package com.github.nija123098.evelyn.fun;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.util.Rand;

/**
 * Made by nija123098 on 5/22/2017.
 */
public class EightBallCommand extends AbstractCommand {
    private final String[] OPTIONS = {
            "As I see it, yes",
            "Better not tell you now",
            "Cannot predict now",
            "Don't count on it",
            "If you say so",
            "In your dreams",
            "It is certain",
            "Most likely",
            "My CPU is saying no",
            "My CPU is saying yes",
            "Out of psychic coverage range",
            "Signs point to yes",
            "Sure, sure",
            "Very doubtful",
            "When life gives you lemon, you drink it",
            "Without a doubt",
            "Wow, Much no, very yes, so maybe",
            "Yes, definitely",
            "Yes, unless you run out of memes",
            "You are doomed",
            "You can't handle the truth"};
    public EightBallCommand() {
        super("8ball", ModuleLevel.FUN, "eightball", "crystal_ball", "See what the mystical 8ball has to say");
    }
    @Command
    public void command(MessageMaker maker){
        maker.append(OPTIONS[Rand.getRand(OPTIONS.length - 1)]);
    }

    @Override
    protected String getLocalUsages() {
        return "8ball <question> // predict the outcome of your yes/no question with the mystical 8ball";
    }
}
