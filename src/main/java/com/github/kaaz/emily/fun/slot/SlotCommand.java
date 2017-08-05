package com.github.kaaz.emily.fun.slot;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ContextType;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Argument;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.GlobalConfigurable;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.economy.MoneyTransfer;
import com.github.kaaz.emily.economy.configs.CurrentMoneyConfig;
import com.github.kaaz.emily.exeption.ArgumentException;
import com.github.kaaz.emily.service.services.ScheduleService;
import com.github.kaaz.emily.util.EmoticonHelper;
import com.github.kaaz.emily.util.Rand;

/**
 * Made by nija123098 on 5/17/2017.
 */
public class SlotCommand extends AbstractCommand {
    private static final String UNKNOWN = EmoticonHelper.getChars("white_small_square", false);
    public SlotCommand() {
        super("slot", ModuleLevel.FUN, null, null, "Gambling.");
    }
    @Command
    public void command(Guild guild, User user, MessageMaker maker, @Argument(optional = true, replacement = ContextType.NONE, info = "The amount bet") Integer bet) {
        if (bet == null) bet = 0;
        if (ConfigHandler.getSetting(CurrentMoneyConfig.class, user) < bet) throw new ArgumentException("You don't have that much currency");
        if (bet < 0) throw new ArgumentException("You can't bet against yourself!  Believe in your self!");
        SlotPack slotPack = ConfigHandler.getSetting(SlotPackConfig.class, user);
        int[][] ints = new int[3][3];
        for (int i = 0; i < ints.length; ++i) {
            ints[i][0] = Rand.getRand(slotPack.length() - 1);
            for (int j = 1; j < ints[i].length; j++) {
                ints[i][j] = (ints[i][j - 1] + 1) % slotPack.length();
            }
        }
        Integer slot = getSlotResult(ints);
        Integer amount = slot == null ? 0 : slot * bet;
        if (slot != null) amount *= slotPack.getAmount(slot);
        else {
            int count = 0;
            for (int i = 0; i < 3; i++) if (ints[i][1] == 0) ++count;
            if (count != 0) amount = bet * count;
        }
        String[] strings = getMessage(ints, slotPack, amount);
        if (bet != 0) MoneyTransfer.transact(guild, user, null, amount == 0 ? -bet : amount, "A slot bet");
        if (amount == 0) {
            Integer finalBet = bet;
            ConfigHandler.changeSetting(SlotJackpotConfig.class, GlobalConfigurable.GLOBAL, aFloat -> aFloat + finalBet / 2);
        }
        maker.withAutoSend(false);
        for (int i = 0; i < strings.length - 1; i++) {
            int r = i;
            ScheduleService.schedule(i * 750, () -> maker.getHeader().clear().getMaker().append(strings[r]).forceCompile().send());
        }
        int last = strings.length - 1;
        ScheduleService.schedule(last * 750, () -> maker.getHeader().clear().getMaker().forceCompile().append(strings[last]).send());
    }
    private static String[] getMessage(int[][] ints, SlotPack pack, Integer amountWon){
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
    private static Integer getSlotResult(int[][] ints){// null if none
        if ((ints[0][0] == ints[1][1] && ints[1][1] == ints[2][2]) || (ints[2][0] == ints[1][1] && ints[1][1] == ints[0][2])) return ints[1][1];
        if (ints[0][0] == ints[1][0] && ints[1][0] == ints[2][0]) return ints[1][1];
        return null;
    }
}
