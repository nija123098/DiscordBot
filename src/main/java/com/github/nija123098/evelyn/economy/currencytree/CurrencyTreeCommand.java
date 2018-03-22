package com.github.nija123098.evelyn.economy.currencytree;

import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
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
import com.github.nija123098.evelyn.economy.lootcrate.LootCrateConfig;
import com.github.nija123098.evelyn.economy.lootcrate.LootCrateEmotes;
import com.github.nija123098.evelyn.economy.plantation.configs.CoffeeEmotes;
import com.github.nija123098.evelyn.economy.plantation.configs.CurrentBeansConfig;
import com.github.nija123098.evelyn.economy.plantation.configs.CurrentGroundsConfig;
import com.github.nija123098.evelyn.economy.plantation.configs.CurrentRoastedBeansConfig;
import com.github.nija123098.evelyn.exception.ArgumentException;
import com.github.nija123098.evelyn.fun.slot.SlotJackpotConfig;
import com.github.nija123098.evelyn.perms.BotRole;

import java.util.Objects;

/**
 * @author Dxeo
 * @since 1.0.0
 */
public class CurrencyTreeCommand extends AbstractCommand {// todo clean and optimize
    public CurrencyTreeCommand() {
        super("currencytree", BotRole.BOT_ADMIN, ModuleLevel.ECONOMY, "moneytree, mt, ct", null, null);
    }

    @Command
    public static void command(MessageMaker maker, Guild guild, @Argument Integer amount, @Argument(optional = true, info = "user to set amount for") User user, @Argument(optional = true, info = "currency type") String type) {

        //configure message maker
        if (maker != null) {
            maker.mustEmbed();
            maker.withAutoSend(false);
            maker.withImage(ConfigProvider.URLS.currencytreeGif());
        }

        //check for valid amount
        if (amount < 0) {
            throw new ArgumentException("You cannot set less than 0 currency");
        }

        //if no type default to currency
        if (Objects.equals(type, "")) {
            type = "currency";
        }

        //set amount according to currency type
        switch (type.toLowerCase()) {

            //set currency
            case "currency":
                ConfigHandler.setSetting(CurrentCurrencyConfig.class, user, amount + ConfigHandler.getSetting(CurrentCurrencyConfig.class, user));
                if (maker != null) maker.appendRaw(user.getDisplayName(guild) + "'s Currency balance has been incremented by: `\u200B " + ConfigHandler.getSetting(CurrencySymbolConfig.class, guild) + " " + amount + " \u200B`");
                break;

            //set loot crates
            case "lootcrate":
                ConfigHandler.setSetting(LootCrateConfig.class, user, amount + ConfigHandler.getSetting(LootCrateConfig.class, user));
                if (maker != null) maker.appendRaw(user.getDisplayName(guild) + "'s Lootcrate balance has been incremented by: `\u200B " + LootCrateEmotes.CRATE + " " + amount + " \u200B`");
                break;

            //set the jackpot for a guild the command is used in
            case "jackpot":
                if (guild == null) throw new ArgumentException("You can not use this without a guild context");
                ConfigHandler.setSetting(SlotJackpotConfig.class, guild, amount + ConfigHandler.getSetting(SlotJackpotConfig.class, guild));
                if (maker != null) maker.appendRaw(guild.getName() + "'s Jackpot balance has been incremented by: `\u200B " + ConfigHandler.getSetting(CurrencySymbolConfig.class, guild) + " " + amount + " \u200B`");
                break;

            //set coffee beans
            case "beans":
                ConfigHandler.setSetting(CurrentBeansConfig.class, user, amount + ConfigHandler.getSetting(CurrentBeansConfig.class, user));
                if (maker != null) maker.appendRaw(user.getDisplayName(guild) + "'s coffee beans have been incremented by: " + CoffeeEmotes.BEANS + " `\u200B " + amount + " \u200B`");
                break;

            //set roasted beans
            case "roasted":
                ConfigHandler.setSetting(CurrentRoastedBeansConfig.class, user, amount + ConfigHandler.getSetting(CurrentRoastedBeansConfig.class, user));
                if (maker != null) maker.appendRaw(user.getDisplayName(guild) + "'s roasted beans have been incremented by: " + CoffeeEmotes.ROASTBEANS + " `\u200B " + amount + " \u200B`");
                break;

            //set coffee grounds
            case "grounds":
                ConfigHandler.setSetting(CurrentGroundsConfig.class, user, amount + ConfigHandler.getSetting(CurrentGroundsConfig.class, user));
                if (maker != null) maker.appendRaw(user.getDisplayName(guild) + "'s coffee grounds have been incremented by: " + CoffeeEmotes.GROUNDS + " `\u200B " + amount + " \u200B`");
                break;


            //no type found
            default:
                throw new ArgumentException("Type not found. Please use a valid type");

        }

    }
    @Override
    public String getUsages() {
        return "#  ct <amount> // Add amount of currency to self\n" +
                        "#  ct <amount> <currency_type> // Add amount of currency_type to self OR current guild\n" +
                        "#  ct <amount> <@user> // Add amount of currency to @user\n" +
                        "#  ct <amount> <@user> <currency_type> // Add amount of currency_type to @user\n" +
                        "#  ct set <amount> // Set amount of currency for self\n" +
                        "#  ct set <amount> <currency_type> // Set amount of currency_type for self OR current guild\n" +
                        "#  ct set <amount> <@user> // Set amount of currency for @user\n" +
                        "#  ct set <amount> <@user> <currency_type> // Set amount of currency_type for @user";
    }

    @Override
    public String getHelp() {
        return "#  Currency Types: currency, lootcrate, jackpot, beans, roasted, grounds";
    }

}