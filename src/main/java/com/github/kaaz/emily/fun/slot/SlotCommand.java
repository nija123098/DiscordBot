package com.github.kaaz.emily.fun.slot;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.anotations.Argument;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.GlobalConfigurable;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.DiscordClient;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.economy.MoneyTransfer;
import com.github.kaaz.emily.service.services.ScheduleService;
import com.github.kaaz.emily.util.EmoticonHelper;
import com.github.kaaz.emily.util.Rand;

/**
 * Made by nija123098 on 5/17/2017.
 */
public class SlotCommand extends AbstractCommand {
    private static final String UNKNOWN = EmoticonHelper.getChars("white_small_square");
    public SlotCommand() {
        super("slot", ModuleLevel.FUN, null, null, "Gambling.");
    }
    @Command
    public void command(User user, MessageMaker maker, @Argument(optional = true, info = "The amount bet") Float bet) {
        if (bet == null) bet = 0F;
        SlotPack slotPack = ConfigHandler.getSetting(SlotPackConfig.class, user);
        int[][] ints = new int[3][3];
        for (int i = 0; i < ints.length; ++i) {
            ints[i][0] = Rand.getRand(slotPack.length() - 1);
            for (int j = 1; j < ints[i].length; j++) {
                ints[i][j] = (ints[i][j - 1] + 1) % slotPack.length();
            }
        }
        Integer slot = getSlotResult(ints);
        Float amount = slot == null ? 0 : slot.floatValue() * bet;
        if (slot != null) amount *= slotPack.getAmount(slot);
        else {
            int count = 0;
            for (int[] anInt : ints) {
                for (int anAnInt : anInt) {
                    if (anAnInt == 0) {
                        ++count;
                        break;
                    }
                }
            }
            if (count != 0) {
                amount = (float) Math.pow(bet, count);
            }
        }
        String[] strings = getMessage(ints, slotPack, amount);
        MoneyTransfer.transact(DiscordClient.getOurUser(), user, 0, amount == null ? -bet : amount, "A slot bet");
        if (amount == null) {
            Float finalBet = bet;
            ConfigHandler.changeSetting(SlotJackpotConfig.class, GlobalConfigurable.GLOBAL, aFloat -> aFloat + finalBet / 2);
        }
        maker.withAutoSend(false);
        for (int i = 0; i < strings.length - 1; i++) {
            int r = i;
            ScheduleService.schedule(i * 750, () -> maker.getHeader().clear().getMaker().append(strings[r]).forceCompile().send());
        }
        int last = strings.length - 1;
        ScheduleService.schedule(last * 750, () -> maker.getHeader().clear().getMaker().forceCompile().append(strings[last]).send());//*/
    }
    private String[] getMessage(int[][] ints, SlotPack pack, Float amountWon){
        String[] strings = new String[4];
        for (int h = 0; h < strings.length; h++) {
            String builder = "";
            for (int i = 0; i < ints.length; i++) {
                builder += "\n|";
                for (int j = 0; j < ints[i].length; ++j) {
                    builder += (h > j ? pack.getChar(ints[j][i]) : UNKNOWN) + "|";
                }
            }
            if (h > 2) builder += "\n\n" + "You won " + (amountWon == null ? "*nothing*, how unexpected" : amountWon);
            strings[h] = builder;
        }
        return strings;
    }
    private Integer getSlotResult(int[][] ints){
        int bestRow = Integer.MAX_VALUE;
        int type;
        for (int i = 0; i < ints.length; i++) {
            type = ints[0][i];
            for (int j = 1; j < ints[i].length; j++) {
                if (type != ints[j][i]){
                    type = Integer.MAX_VALUE;
                    break;
                }
            }
            if (type < bestRow) bestRow = type;
        }
        if (ints.length == ints[0].length){
            type = ints[0][0];
            for (int i = 1; i < ints.length; ++i) {
                if (type != ints[i][i]){
                    type = Integer.MAX_VALUE;
                    break;
                }
            }
            if (type < bestRow) bestRow = type;
            type = ints[0][ints.length-1];
            for (int i = 1; i < ints.length; ++i) {
                if (type != ints[i][ints.length-1-i]){
                    type = Integer.MAX_VALUE;
                    break;
                }
            }
            if (type < bestRow) bestRow = type;
        }
        return bestRow == Integer.MAX_VALUE ? null : bestRow;
    }
}
