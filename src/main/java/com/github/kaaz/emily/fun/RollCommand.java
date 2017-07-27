package com.github.kaaz.emily.fun;

import com.github.kaaz.emily.audio.YoutubeTrack;
import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ContextType;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Argument;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.command.annotations.Context;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.util.EmoticonHelper;
import com.github.kaaz.emily.util.Rand;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Made by nija123098 on 5/25/2017.
 */
public class RollCommand extends AbstractCommand {
    public RollCommand() {
        super("roll", ModuleLevel.FUN, "dice, rng", "game_die", "For if you ever need a random number");
    }// this should have a chance of rick rolling you
    @Command
    public void command(@Argument(optional = true, replacement = ContextType.NONE) Integer first, @Argument(optional = true, replacement = ContextType.NONE) Integer second, String arg, MessageMaker maker, @Context(softFail = true) Guild guild) {
        int value;
        Pattern dice = Pattern.compile("(\\d+)d(\\d+)\\+?(\\d+)?");
        if (first != null) {
            if (second == null) {
                value = Rand.getRand(first - 1) + 1;
                maker.append("Rolling between: 1 and " + first + " | Rolled: " + value);
            } else {
                if (first < second) {
                    value = Rand.getRand(second - first) + first;
                    maker.append("Rolling between: " + first + " and " + second + " | Rolled: " + value);
                } else {
                    maker.append("I can't roll between two numbers like that, format it properly " + EmoticonHelper.getChars("slight_frown", false));
                }
            }
        } else if (arg.isEmpty()) {
            value = Rand.getRand(5) + 1;
            maker.append("Rolling 1 [6] sided " + EmoticonHelper.getChars("game_die", false) + " | Rolled: " + value);
        } else {
            int max_dice = 40, min_sides = 2;
            Matcher match = dice.matcher(arg);
            if (match.find()) {
                int die = Integer.parseInt(match.group(1));
                int sides = Integer.parseInt(match.group(2));
                int bonus = 0;
                if (die > max_dice) {
                    maker.append("I only have 40 " + EmoticonHelper.getChars("game_die", false) + "! " + EmoticonHelper.getChars("wink", false));
                } else if (die < 1) {
                    maker.append("Hmm, I'm gonna need at least one " + EmoticonHelper.getChars("game_die", false));
                } else if (sides < min_sides) {
                    maker.append(EmoticonHelper.getChars("confused", false) + " hard to do anything with less than 2 sides");
                } else if (match.group(3) != null && !"null".equals(match.group(3))) {
                    bonus = Integer.parseInt(match.group(3));
                    maker.append(multiDice(die, sides, bonus));
                } else {
                    maker.append(multiDice(die, sides, bonus));
                }
            }
        }
        GuildAudioManager manager = GuildAudioManager.getManager(guild);
        if (manager != null && manager.currentTrack() == null && Rand.getRand(999) == 2) manager.queueTrack(new YoutubeTrack("dQw4w9WgXcQ"));
    }

    private static String multiDice(int dices, int sides, int bonus) {
        String text = String.format("Rolling %s [%s] sided " + EmoticonHelper.getChars("game_die", false) + ": ", dices, sides);
        int total = 0;
        for (int i = 0; i < dices; i++) {
            int roll = Rand.getRand(sides) + 1;
            text += " " + roll;
            total += roll;
        }
        if (bonus != 0) {
            text += " + " + bonus;
            total += bonus;
        }
        return text + " Total: **" + total + "**";
    }


}
