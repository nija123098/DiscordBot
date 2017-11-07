package com.github.nija123098.evelyn.fun.slot;

import com.fasterxml.jackson.databind.util.ArrayIterator;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ContextType;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.command.annotations.Context;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.Configurable;
import com.github.nija123098.evelyn.config.GlobalConfigurable;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.helpers.ReactionBehavior;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.Message;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.economy.MoneyTransfer;
import com.github.nija123098.evelyn.economy.configs.CurrentMoneyConfig;
import com.github.nija123098.evelyn.economy.configs.MoneySymbolConfig;
import com.github.nija123098.evelyn.exeption.ArgumentException;
import com.github.nija123098.evelyn.exeption.BotException;
import com.github.nija123098.evelyn.service.services.MemoryManagementService;
import com.github.nija123098.evelyn.service.services.ScheduleService;
import com.github.nija123098.evelyn.util.EmoticonHelper;
import com.github.nija123098.evelyn.util.FormatHelper;
import com.google.api.client.util.Joiner;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Made by nija123098 on 5/17/2017.
 */
public class SlotCommand extends AbstractCommand {
    private static final Map<User, Integer> BET_MAP = new ConcurrentHashMap<>();
    private static final List<User> ACTIVE_EDITING = new MemoryManagementService.ManagedList<>(6000);// todo make proper command rate-limiter
    private static final String UNKNOWN = EmoticonHelper.getChars("white_small_square", false);
    public SlotCommand() {
        super("slot", ModuleLevel.FUN, null, "slot_machine", "Gamble your cookies away");
    }
    @Command
    public void command(@Context(softFail = true) Guild guild, User user, MessageMaker maker, @Argument(optional = true, replacement = ContextType.NONE, info = "The amount bet") Integer bet, @Context(softFail = true) Boolean first, Message message) {
        if (first == null) new MessageMaker(maker).append("Use reactions to use slots.  Use " + EmoticonHelper.getChars("repeat_one", true) + " to re-bet and the arrows to change the balance.\n\n").withDeleteDelay(30_000L).send();
        if (ACTIVE_EDITING.contains(user)) return;
        ACTIVE_EDITING.add(user);
        if (bet == null) bet = 0;
        if (first == null) BET_MAP.put(user, bet);
        else bet = BET_MAP.get(user);
        if (ConfigHandler.getSetting(CurrentMoneyConfig.class, user) < bet){
            BotException exception = new ArgumentException("You don't have that much currency");
            if (first == null) throw exception;
            exception.makeMessage(user.getOrCreatePMChannel()).send();
            return;
        }
        if (bet < 0) throw new ArgumentException("You can't bet against yourself!  Believe in your self!");
        SlotPack slotPack = ConfigHandler.getSetting(SlotPackConfig.class, user);
        int[][] ints = slotPack.getTable();
        Integer amount = slotPack.getReturn(ints) * bet;
        String[] strings = getMessage(ints, slotPack, amount, user, first);
        if (bet != 0) MoneyTransfer.transact(guild, user, null, amount == 0 ? -bet : amount, "A slot bet");
        if (amount == 0) {
            Integer finalBet = bet;
            ConfigHandler.changeSetting(SlotJackpotConfig.class, GlobalConfigurable.GLOBAL, aFloat -> aFloat + finalBet / 2);
        }
        maker.withAutoSend(false);
        for (int i = 0; i < strings.length - 1; i++) {
            int r = i;
            ScheduleService.schedule(i * 1000, () -> setupMaker(strings[r], maker).send());
        }
        ScheduleService.schedule((strings.length - 1) * 1000, () -> setupMaker(strings[strings.length - 1], maker).send());
        if (first == null) {
            maker.withReactionBehavior("repeat_one", (add, reaction, u) -> {
                try{if (u.equals(user)) this.command(guild, user, maker, 0, false, null);
                }catch (BotException e) {e.makeMessage(message.getChannel()).send();}
            });
            ReactionBehavior arrowBehavior = (add, reaction, u) -> {
                String name = reaction.getName().substring(6);
                int old = BET_MAP.get(u);
                BET_MAP.compute(u, (us, integer) -> {
                    integer += (name.startsWith("down") ? -1 : 1) * (name.endsWith("small") ? 10 : 1);
                    int max = ConfigHandler.getSetting(CurrentMoneyConfig.class, user);
                    return max < integer ? max : integer;
                });
                maker.forceCompile().getHeader().clear().appendRaw(FormatHelper.filtering(maker.sentMessage().getContent(), c -> c != 13).replace("  Bet: " + old, "  Bet: " + BET_MAP.get(u)));
                maker.send();
            };
            maker.withReactionBehavior("arrow_up_small", arrowBehavior);
            maker.withReactionBehavior("arrow_up", arrowBehavior);
            maker.withReactionBehavior("arrow_down", arrowBehavior);
            maker.withReactionBehavior("arrow_down_small", arrowBehavior);
        }
        if (message != null) message.delete();
    }
    private static String[] getMessage(int[][] ints, SlotPack pack, Integer amountWon, User user, Boolean first){
        String currencySymbol = ConfigHandler.getSetting(MoneySymbolConfig.class, GlobalConfigurable.GLOBAL);
        String[] strings = new String[4];
        for (int h = 1; h < strings.length; h++) {
            String builder = (first == null && h == 1 ? user.getName() : user.mention()) + "  Bet: " + BET_MAP.get(user) + currencySymbol;
            for (int i = 0; i < ints.length; i++) {
                builder += "\n";
                for (int j = 0; j < ints[i].length; ++j) {
                    builder += (h > j ? pack.getChar(ints[j][i]) : UNKNOWN) + (j < 2 ? "|" : " " + (i >= h ? EmoticonHelper.getChars("grey_exclamation", false) : i + 1 == h && h != strings.length - 1 ? EmoticonHelper.getChars("red_circle", false) : ""));
                }
            }
            builder += "\n";
            if (h > 2) builder += "Winnings: " + amountWon + currencySymbol + "  Balance: " + (ConfigHandler.getSetting(CurrentMoneyConfig.class, user) + (amountWon == 0 ? - BET_MAP.get(user) : amountWon)) + currencySymbol;
            else builder += "Processing";
            strings[h] = builder;
        }
        return Arrays.copyOfRange(strings, 1, strings.length);
    }
    private static MessageMaker setupMaker(String string, MessageMaker maker){
        maker.forceCompile().getHeader().clear();
        String[] split = string.split("\n");
        maker.appendRaw("\n" + Joiner.on('\n').join(new ArrayIterator<>(Arrays.copyOfRange(split, 0, 4))) + "\n").maySend().append(split[4]);
        return maker;
    }
    @Override
    public long getCoolDown(Class<? extends Configurable> clazz) {
        return clazz.equals(User.class) ? 20_000 : -1;
    }
}
