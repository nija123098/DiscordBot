package com.github.nija123098.evelyn.fun;

import com.github.nija123098.evelyn.audio.YoutubeTrack;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ContextType;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.command.annotations.Context;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.util.CareLess;
import com.github.nija123098.evelyn.util.EmoticonHelper;
import com.github.nija123098.evelyn.util.Log;
import com.github.nija123098.evelyn.util.Rand;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class RollCommand extends AbstractCommand {
    public RollCommand() {
        super("roll", ModuleLevel.FUN, "dice, rng", null, "For if you ever need a random number");
    }// this should have a chance of rick rolling you
    @Command
    public void command(@Argument(optional = true, replacement = ContextType.NONE) Integer first, @Argument(optional = true, replacement = ContextType.NONE) Integer second,@Argument(info = "test") String arg, MessageMaker maker, @Context(softFail = true) Guild guild) {
        int value;
        String[] notOperands = arg.split("(\\s*[^0-z]+\\s*)");
        String[] operands = arg.split("([\\w]+)");
        String[] res = new String[operands.length + notOperands.length];
        final Pattern dice = Pattern.compile("(\\d+)d(\\d+)");
        if (first != null) {
            if (second == null) {
                value = Rand.getRand(first) + 1;
                maker.append("Rolling between: 1 and " + first + " | Rolled: " + value);
            } else {
                if (first < second) {
                    value = Rand.getRand(second - first + 1) + first;
                    maker.append("Rolling between: " + first + " and " + second + " | Rolled: " + value);
                } else {
                    maker.append("I can't roll between two numbers like that, format it properly " + EmoticonHelper.getChars("slight_frown", false));
                }
            }
        } else if (arg.isEmpty()) {
            value = Rand.getRand(6) + 1;
            maker.append("Rolling 1 [6] sided " + EmoticonHelper.getChars("game_die", false) + " | Rolled: " + value);
        } else {
            int max_dice = 144, min_sides = 2;
            for(int i = 0; i < res.length; i++) {
                res[i] = i % 2 == 0 ? notOperands[i / 2] : operands[i / 2 + 1];
            }
            int diceCount = 0;
            int bonusCount = 0;
            for (String re : res) {
                if (re.matches("(\\d+)d(\\d+)")) {
                    diceCount++;
                } else if (re.matches("(\\d+)")) {
                    bonusCount++;
                }
            }
            Log.log("diceCount: " + diceCount);
            Log.log("other numbers: " + bonusCount);

            Matcher match = dice.matcher(arg);
            if (match.find() && diceCount == 1) {
                int die = Integer.parseInt(match.group(1));
                int sides = Integer.parseInt(match.group(2));
                int bonus = 0;
                if (die > max_dice) {
                    maker.append("I only have 144 " + EmoticonHelper.getChars("game_die", false) + "! " + EmoticonHelper.getChars("wink", false));
                } else if (die < 1) {
                    maker.append("Hmm, I'm gonna need at least one " + EmoticonHelper.getChars("game_die", false));
                } else if (sides < min_sides) {
                    maker.append(EmoticonHelper.getChars("confused", false) + " hard to do anything with less than 2 sides");
                } else {
                    maker.append(multiDice(die, sides, bonus));
                }
            } else {
                maker.appendRaw("Message Soarnir for your prize.");
            }
        }
        GuildAudioManager manager = GuildAudioManager.getManager(guild);
        if (manager != null && manager.currentTrack() == null && Rand.getRand(1000) == 0){
            CareLess.something(() -> manager.queueTrack(new YoutubeTrack("dQw4w9WgXcQ")));
        }
    }

    private static String multiDice(int dices, int sides, int bonus) {
        StringBuilder text = new StringBuilder(String.format("Rolling %s [%s] sided " + EmoticonHelper.getChars("game_die", false), dices, sides));
        int total = 0;
        int magnitude = String.valueOf(sides).length();
        String zero = "";
        String[] zeroes = new String[magnitude];
        StringBuilder dashes = new StringBuilder("-");
        zeroes[magnitude-1] = "";
        for (int l = magnitude-1; l > 0; l--) {
            zero = zero + "0";
            dashes.append("-");
            zeroes[l-1] = zero;
        }
        if (bonus != 0) {
            text.append(" + (").append(bonus).append("): \n");
            total = total + bonus;
        } else {
            text.append(": \n");
        }
        int gridArray[][] = fancyGrid(dices, sides);
        for (int t = 0; t < gridArray.length; t++) {
            for (int j = 0; j < gridArray[t].length; j++) {
                total = total + gridArray[t][j];
                magnitude = String.valueOf(gridArray[t][j]).length() - 1;
                if (gridArray[t][j] == 0) {
                    text.append("| **`").append(dashes).append("`** ");
                } else {
                    text.append("| **`").append(zeroes[magnitude]).append(gridArray[t][j]).append("`** ");
                }
            }
            text.append("|\n");
        }
        return text + " Total: **" + total + "**";
    }

    private static int[][] fancyGrid(int dice, int sides) {
        int[] tableArray = counter(dice);
        int[][] gridArray = new int[tableArray[0]][tableArray[1]];
        int row = 0, col = 0;
        //fill the grid
        for (int c = 0; c < dice; c++) {
            gridArray[col][row] = Rand.getRand(sides + 1) + 1;
            if (row < gridArray[0].length - 1) {
                row = row + 1; //row
            } else {
                col = col + 1; //col
                row = 0;
            }
        }
        return gridArray;
    }

    private static int[] counter(int i) {
        int total = 0;
        int rowTest = 0;
        int colTest = 0;
        int[] rowColArray = new int[2];
        while (total < i) {
            if (rowTest <= colTest) {
                rowTest++;
            } else {
                colTest++;
            }
            total = rowTest * colTest;
        }
        rowColArray[0] = rowTest;
        rowColArray[1] = colTest;
        return rowColArray;
    }
}
