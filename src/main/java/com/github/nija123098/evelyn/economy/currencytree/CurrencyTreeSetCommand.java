package com.github.nija123098.evelyn.economy.currencytree;

import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.economy.configs.CurrencySymbolConfig;
import com.github.nija123098.evelyn.economy.configs.CurrentCurrencyConfig;
import com.github.nija123098.evelyn.economy.lootcrate.LootCrateConfig;
import com.github.nija123098.evelyn.economy.lootcrate.LootCrateEmotes;
import com.github.nija123098.evelyn.exception.ArgumentException;
import com.github.nija123098.evelyn.fun.slot.SlotJackpotConfig;

/**
 * @author Dxeo
 * @since 1.0.0
 */
public class CurrencyTreeSetCommand extends AbstractCommand {
    public CurrencyTreeSetCommand() {
        super(CurrencyTreeCommand.class,"set", null, null, null, null);
    }

    @Command
    public void command(MessageMaker maker, Guild guild, @Argument Integer amount, @Argument(optional = true, info = "user to set amount for") User user, @Argument(optional = true, info = "currency type") String type) {

        //configure message maker
        maker.withAutoSend(false);
        maker.withImage(ConfigProvider.URLS.currencytreeGif());

        //check for valid amount
        if (amount < 0) {
            throw new ArgumentException("You cannot set less than 0 currency");
        }

        //set amount according to currency type
        // TODO ADD TYPES TO THE HELP DESCRIPTION
        switch (type.toLowerCase()) {

            //set currency
            case "currency":
            case "":
                ConfigHandler.setSetting(CurrentCurrencyConfig.class, user, amount);
                maker.appendRaw(user.getDisplayName(guild) + "'s Currency balance has been set to: `\u200B " + ConfigHandler.getSetting(CurrencySymbolConfig.class, guild) + " " + amount + " \u200B`");
                maker.send();
                break;

            //set loot crates
            case "lootcrate":
                ConfigHandler.setSetting(LootCrateConfig.class, user, amount);
                maker.appendRaw(user.getDisplayName(guild) + "'s Lootcrate balance has been set to: `\u200B " + LootCrateEmotes.CRATE + " " + amount + " \u200B`");
                maker.send();
                break;

            //set the jackpot for a guild the command is used in
            case "jackpot":
                ConfigHandler.setSetting(SlotJackpotConfig.class, guild, amount);
                maker.appendRaw(guild.getName() + "'s Jackpot balance has been set to: `\u200B " + ConfigHandler.getSetting(CurrencySymbolConfig.class, guild) + " " + amount + " \u200B`");
                maker.send();
                break;

            //no type found
            default:
                throw new ArgumentException("Type not found. Please use a valid type");

        }

    }

}