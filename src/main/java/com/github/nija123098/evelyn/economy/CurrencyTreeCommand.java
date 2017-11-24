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
import com.github.nija123098.evelyn.economy.plantation.configs.CurrentBeansConfig;
import com.github.nija123098.evelyn.exception.ArgumentException;
import com.github.nija123098.evelyn.perms.BotRole;

/**
 * Made by nija123098 on 7/7/2017.
 */
public class CurrencyTreeCommand extends AbstractCommand {
    public CurrencyTreeCommand() {
        super("currencytree", BotRole.BOT_ADMIN, ModuleLevel.ECONOMY, "ct", null, null);
    }

    @Command
    public void command(MessageMaker maker, Guild guild, @Argument Integer amount, @Argument(optional = true, info = "user to set amount for") User user, @Argument(optional = true, info = "currency type") String type) {

        //configure message maker
        maker.mustEmbed();
        maker.withAutoSend(false);
        maker.withImage("https://media.giphy.com/media/ZPFQVis9WAAcE/giphy.gif");

        //check for valid amount
        if (amount < 0) {
            throw new ArgumentException("You cannot set less than 0 currency");
        }

        //if no type default to currency
        if (type.contentEquals("")) {
            type = "currency";
        }

        //set amount according to currency type
        //REMEMBER TO ADD TYPES TO THE HELP DESCRIPTION
        switch (type.toLowerCase()) {

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

            case "beans":
                ConfigHandler.setSetting(CurrentBeansConfig.class, user, amount);
                maker.appendRaw(user.getDisplayName(guild) + "'s coffee beans have been set to: `\u200b " + amount + " \u200b`");
                maker.send();
                break;

            //no type found
            default:
                throw new ArgumentException("Type not found. Please use a valid type");

        }

    }

    //help command override usages
    @Override
    public String getUsages() {

        //command usage:
        StringBuilder desc = new StringBuilder();
        desc.append("#  ct <amount> // Set amount of currency for self\n");
        desc.append("#  ct <amount> <currency_type> // Set amount of currency_type for self OR current guild\n");
        desc.append("#  ct <amount> <@user> // Set currency for @user\n");
        desc.append("#  ct <amount> <@user> <currency_type> // Set currency_type for @user");
        return desc.toString();
    }

    //help command override description
    @Override
    public String getHelp() {

        //command description:
        StringBuilder desc = new StringBuilder();
        desc.append("#  Currency Types:\n// currency\n// lootcrate\n// jackpot\n// beans");
        return desc.toString();
    }

}