package com.github.nija123098.evelyn.fun.fishing;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.command.annotations.Context;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.economy.configs.CurrencySymbolConfig;
import com.github.nija123098.evelyn.economy.configs.CurrentCurrencyConfig;
import com.github.nija123098.evelyn.economy.configs.LootCrateConfig;
import com.github.nija123098.evelyn.exception.ArgumentException;
import com.github.nija123098.evelyn.util.Rand;

import java.util.concurrent.TimeUnit;

/**
 * Written by Soarnir 20/11/17
 */

public class FishCommand extends AbstractCommand {

    //constructor
    public FishCommand() {
        super("fish", ModuleLevel.FUN, null, null, "fish for stuff");
    }

    //fishingEmotes: Fish, Tropical_Fish, Blowfish, Gift
    //DO NOT CHANGE THE ORDER WITHOUT CHANGING THE gerReward() METHOD
    private String[] fishingEmotes = {"\uD83D\uDC1F", "\uD83D\uDC20", "\uD83D\uDC21", "\uD83C\uDF81"};

    //trashEmotes: Boot, Women_Hat, Anchor, Skull, Sun_glasses, Newspaper
    private String[] trashEmotes = {"\uD83D\uDC62", "\uD83D\uDC52", "⚓", "\uD83D\uDC80", "\uD83D\uDC53", "\uD83D\uDCF0"};

    //cost per use
    private int cost = 8;

    //reward variable
    private int reward;

    //reward if gift
    private boolean gift;

    @Command
    public void command(@Context(softFail = true) Guild guild, User user, MessageMaker maker) throws InterruptedException {

        //save guild money symbol
        String currency_symbol = ConfigHandler.getSetting(CurrencySymbolConfig.class, guild);

        //subtract cost
        int userBalance = ConfigHandler.getSetting(CurrentCurrencyConfig.class, user);
        if (userBalance < cost) {

            //not enough funds
            throw new ArgumentException("You need `\u200B " + currency_symbol + " " + (cost - userBalance) + " \u200B` more to perform this transaction.");
        }
        ConfigHandler.setSetting(CurrentCurrencyConfig.class, user, userBalance - cost);
        userBalance -= cost;

        //configure message maker
        maker.withAutoSend(false);
        maker.mustEmbed();

        //print the first frame
        maker.appendRaw("```\uD83C\uDFA3 @" + user.getDisplayName(guild) + " \uD83C\uDFA3\n");
        maker.appendRaw("════════════════════════════════════════\n");
        maker.appendRaw(
                        "\u200b       ,-.\n" +
                        "\u200b    O /   `.        Cast: " + currency_symbol + " " + cost + "\n" +
                        "\u200b   <\\/      `.      Loot: -\n" +
                        "\u200b    |*        `.   Funds: " + currency_symbol + " " + userBalance  + "\n" +
                        "\u200b   / \\          `.\n" +
                        "\u200b  /  /            `,\n" +
                        "\u200b──────────┐ ~~~~~~~~~~~~~~~~~~~~~~~~" +
                        "```");
        maker.send();

        //reset the message maker
        maker.getHeader().clear();

        //get reward emote
        int eReward = getReward();

        //add win
        String rewardAtPole;
        String rewardAtLoot;
        if (eReward == - 1){

            //select trash from trash emotes
            int selectTrash = Rand.getRand(trashEmotes.length);
            rewardAtPole = trashEmotes[selectTrash];
            rewardAtLoot = trashEmotes[selectTrash];

        } else if (gift) {

            //add gift to user
            rewardAtPole = fishingEmotes[eReward];
            rewardAtLoot = fishingEmotes[eReward] + " 1";

            //save gift to loot box inventory
            ConfigHandler.setSetting(LootCrateConfig.class, user, (ConfigHandler.getSetting(LootCrateConfig.class, user) + 1));

        } else {

            //add fishing reward
            rewardAtPole = fishingEmotes[eReward];
            rewardAtLoot = currency_symbol + " " + reward;
            userBalance = ConfigHandler.getSetting(CurrentCurrencyConfig.class, user) + reward;
            ConfigHandler.setSetting(CurrentCurrencyConfig.class, user, userBalance);
        }

        //print the second frame after delay
        TimeUnit.SECONDS.sleep(2);
        maker.appendRaw("```\uD83C\uDFA3 @" + user.getDisplayName(guild) + " \uD83C\uDFA3\n");
        maker.appendRaw("════════════════════════════════════════\n");
        maker.appendRaw(
                        "\u200b       ,-.\n" +
                        "\u200b    O /   `.        Cast: " + currency_symbol + " " + cost + "\n" +
                        "\u200b   <\\/      `.      Loot: " + rewardAtLoot + "\n" +
                        "\u200b    |*        `.   Funds: " + currency_symbol + " " + userBalance  + "\n" +
                        "\u200b   / \\          `.\n" +
                        "\u200b  /  /            `" + rewardAtPole + "\n" +
                        "\u200b──────────┐ ~~~~~~~~~~~~~~~~~~~~~~~~" +
                        "```");

        //add reaction for repeating the command
        maker.withReactionBehavior("fishing_pole_and_fish", ((add, reaction, u) -> {

            //save user balance
            int mUserBalance = ConfigHandler.getSetting(CurrentCurrencyConfig.class, user);

            //print the first frame
            maker.appendRaw("```\uD83C\uDFA3 @" + user.getDisplayName(guild) + " \uD83C\uDFA3\n");
            maker.appendRaw("════════════════════════════════════════\n");
            maker.appendRaw(
                    "\u200b       ,-.\n" +
                            "\u200b    O /   `.        Cast: " + currency_symbol + " " + cost + "\n" +
                            "\u200b   <\\/      `.      Loot: -\n" +
                            "\u200b    |*        `.   Funds: " + currency_symbol + " " + mUserBalance + "\n" +
                            "\u200b   / \\          `.\n" +
                            "\u200b  /  /            `,\n" +
                            "\u200b──────────┐ ~~~~~~~~~~~~~~~~~~~~~~~~" +
                            "```");
            maker.send();

            //reset the message maker
            maker.getHeader().clear();

            try {
                command(guild,user,maker);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }));
        maker.send();


    }

    private int getReward(){

        //select reward from fishingEmotes
        int selectReward = Rand.getRand(fishingEmotes.length);

        //calculate reward
        gift = false;
        reward = -1;
        int roll;
        int rewardToEmote = -1;
        switch (selectReward){

            //roll for fish
            case 0:
                roll = Rand.getRand(3);
                if (roll == 0){
                    reward = 30;
                    rewardToEmote = 0;
                    break;
                }else {
                    reward = -1;
                    break;
                }

            //roll for tropical fish
            case 1:
                roll = Rand.getRand(4);
                if (roll == 0){
                    reward = 35;
                    rewardToEmote = 1;
                    break;
                }else {
                    reward = -1;
                    break;
                }

            //roll for blowfish
            case 2:
                roll = Rand.getRand(5);
                if (roll == 0){
                    reward = 40;
                    rewardToEmote = 2;
                    break;
                }else {
                    reward = -1;
                    break;
                }

            //roll for gift
            case 3:
                roll = Rand.getRand(8);
                if (roll == 0){
                    gift = true;
                    rewardToEmote = 3;
                    break;
                }else {
                    reward = -1;
                    break;
                }
        }

        //return emote index for fishing emotes
        return rewardToEmote;

    }

}
