package com.github.nija123098.evelyn.economy;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.economy.configs.CurrencySymbolConfig;
import com.github.nija123098.evelyn.economy.configs.CurrentCurrencyConfig;
import com.github.nija123098.evelyn.economy.configs.LootCrateConfig;
import com.github.nija123098.evelyn.economy.configs.SlotJackpotConfig;
import com.github.nija123098.evelyn.exception.ArgumentException;
import com.github.nija123098.evelyn.perms.BotRole;

/**
 * Made by nija123098 on 7/7/2017.
 */
public class CurrencyTreeCommand extends AbstractCommand {
    public CurrencyTreeCommand() {
        super("currencytree", BotRole.BOT_ADMIN, ModuleLevel.ECONOMY, "ct", null, "Cookies do grow on trees");
    }
    @Command
    public void command(MessageMaker maker, Guild guild,@Argument Integer amount, @Argument(optional=true, info = "user to send the amount to")User user, @Argument(optional=true, info = "currency type")String type){

        //configure message maker
        maker.mustEmbed();
        maker.withAutoSend(false);
        maker.withImage("https://media.giphy.com/media/ZPFQVis9WAAcE/giphy.gif");

        //check for valid amount
        if (amount < 0) {
            throw new ArgumentException("You cannot set less than 0 currency");
        }

        //if no type default to currency
        if (type.contentEquals("")){
            type = "currency";
        }

        //set amount according to currency type
        switch (type){

            //set currency
            case "currency":
                ConfigHandler.setSetting(CurrentCurrencyConfig.class, user, amount);
                maker.appendRaw(user.getDisplayName(guild) + "'s Currency balance has been set to: `\u200B " + ConfigHandler.getSetting(CurrencySymbolConfig.class, guild) + " " + amount + " \u200B`");
                maker.send();
                break;

            //set loot crates
            case "lootcrate":
                ConfigHandler.setSetting(LootCrateConfig.class, user, amount);
                maker.appendRaw(user.getDisplayName(guild) + "'s Lootcrate balance has been set to: `\u200B \uD83C\uDF81 " + amount + " \u200B`");
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
