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
import com.github.nija123098.evelyn.util.EmoticonHelper;
import com.github.nija123098.evelyn.util.Log;
import com.github.nija123098.evelyn.util.Rand;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Made by nija123098 on 5/25/2017.
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
        Log.log("res length: " + (operands.length + notOperands.length));
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
            maker.append("Rolling 1 [6] sided " + EmoticonHelper.getChars("game_die", false) + " | Rolled: " + value).mustEmbed();
        } else {
            int max_dice = 144, min_sides = 2;
            Log.log("operands: " + Arrays.toString(operands));
            Log.log("notOperands: " + Arrays.toString(notOperands));
            for(int i = 0; i < res.length; i++) {
                res[i] = i%2==0 ? notOperands[i / 2] : operands[i / 2 + 1];
            }
            Log.log("res: " + Arrays.toString(res));

            int dicecount = 0;
            int bonuscount = 0;
            for (int o = 0; o < res.length; o++) {
                if (res[o].matches("(\\d+)d(\\d+)")) {
                    dicecount++;
                } else if (res[o].matches("(\\d+)")) {
                    bonuscount++;
                }
            }
            Log.log("dicecount: " + dicecount);
            Log.log("other numbers: " + bonuscount);

            Matcher match = dice.matcher(arg);
            if (match.find() && dicecount == 1) {
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
                maker.appendRaw("how did you get here?");
            }
        }
        GuildAudioManager manager = GuildAudioManager.getManager(guild);
        if (manager != null && manager.currentTrack() == null && Rand.getRand(1000) == 0) manager.queueTrack(new YoutubeTrack("dQw4w9WgXcQ"));
    }

    private static String multiDice(int dices, int sides, int bonus) {
        String text = String.format("Rolling %s [%s] sided " + EmoticonHelper.getChars("game_die", false), dices, sides);
        int total = 0;
        int magnitude = String.valueOf(sides).length();
        String zero = "";
        String[] zeroes = new String[magnitude];
        String dashes = "-";
        zeroes[magnitude-1] = "";
        for (int l = magnitude-1; l > 0; l--) {
            zero = zero + "0";
            dashes = dashes + "-";
            zeroes[l-1] = zero;
        }
        if (bonus != 0) {
            text = text + " + (" + bonus + "): \n";
            total = total + bonus;
        } else {
            text = text + ": \n";
        }
        int gridArray[][] = fancyGrid(dices, sides);
        for (int t = 0; t < gridArray.length; t++) {
            for (int j = 0; j < gridArray[t].length; j++) {
                total = total + gridArray[t][j];
                magnitude = String.valueOf(gridArray[t][j]).length() - 1;
                if (gridArray[t][j] == 0) {
                    text = text + ("| **`" + dashes + "`** ");
                } else {
                    text = text + ("| **`" + zeroes[magnitude] + gridArray[t][j] + "`** ");
                }
            }
            text = text + ("|\n");
        }
        return text + " Total: **" + total + "**";
    }

    public static int[][] fancyGrid(int dice, int sides) {
        int [] randArray = new int[dice];
        for (int k = 0; k < dice; k++) {
            randArray[k] = (Rand.getRand(sides + 1) + 1);
        }
        int [] tableArray = Counter(dice);
        int [][] gridArray = new int[tableArray[0]][tableArray[1]];
        int row = 0, col = 0;
        //fill the grid
        for (int c = 0; c < dice; c++) {
            gridArray[col][row] = randArray[c];
            if (row < gridArray[0].length-1) {
                row = row + 1; //row
            } else {
                col = col + 1; //col
                row = 0;
            }
        }
        return gridArray;
    }

    public static int[] Counter(int i) {
        int total = 0;
        int rowtest = 0;
        int coltest = 0;
        int[] rowColArray = new int[2];
        while (total < i) {
            if (rowtest <= coltest) {
                rowtest++;
            } else {
                coltest++;
            }
            total = rowtest * coltest;
        }
        rowColArray[0] = rowtest;
        rowColArray[1] = coltest;
        return rowColArray;
    }
}
