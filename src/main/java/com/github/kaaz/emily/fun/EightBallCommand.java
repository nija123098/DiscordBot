package com.github.kaaz.emily.fun;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.util.Rand;

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
        super("8ball", ModuleLevel.FUN, "eightball", "crystal_ball", "See what the rng 8ball has to say");
    }
    @Command
    public void command(MessageMaker maker){
        maker.append(OPTIONS[Rand.getRand(OPTIONS.length - 1)]);
    }
}
