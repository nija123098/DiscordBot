package com.github.nija123098.evelyn.economy.currencytree;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.economy.configs.CurrencySymbolConfig;
import com.github.nija123098.evelyn.economy.configs.CurrentCurrencyConfig;
import com.github.nija123098.evelyn.economy.lootcrate.LootCrateConfig;
import com.github.nija123098.evelyn.economy.plantation.configs.CurrentBeansConfig;
import com.github.nija123098.evelyn.economy.plantation.configs.CurrentGroundsConfig;
import com.github.nija123098.evelyn.economy.plantation.configs.CurrentRoastedBeansConfig;
import com.github.nija123098.evelyn.exception.ArgumentException;
import com.github.nija123098.evelyn.fun.slot.SlotJackpotConfig;

import java.util.Objects;

import static com.github.nija123098.evelyn.botconfiguration.ConfigProvider.URLS;
import static com.github.nija123098.evelyn.command.ModuleLevel.ECONOMY;
import static com.github.nija123098.evelyn.config.ConfigHandler.getSetting;
import static com.github.nija123098.evelyn.config.ConfigHandler.setSetting;
import static com.github.nija123098.evelyn.economy.lootcrate.LootCrateEmotes.CRATE;
import static com.github.nija123098.evelyn.economy.plantation.configs.CoffeeEmotes.*;
import static com.github.nija123098.evelyn.perms.BotRole.BOT_ADMIN;

/**
 * @author Dxeo
 * @since 1.0.0
 */
public class CurrencyTreeCommand extends AbstractCommand {
    public CurrencyTreeCommand() {
        super("currencytree", BOT_ADMIN, ECONOMY, "moneytree, mt, ct", null, null);
    }

    @Command
    public void command(MessageMaker maker, Guild guild, @Argument Integer amount, @Argument(optional = true, info = "user to set amount for") User user, @Argument(optional = true, info = "currency type") String type) {

        //configure message maker
        maker.mustEmbed();
        maker.withAutoSend(false);
        maker.withImage(URLS.currencytreeGif());

        //check for valid amount
        if (amount < 0) {
            throw new ArgumentException("You cannot set less than 0 currency");
        }

        //if no type default to currency
        if (Objects.equals(type, "")) {
            type = "currency";
        }

        //set amount according to currency type
        //REMEMBER TO ADD TYPES TO THE HELP DESCRIPTION
        switch (type.toLowerCase()) {

            //set currency
            case "currency":
                setSetting(CurrentCurrencyConfig.class, user, amount + getSetting(CurrentCurrencyConfig.class, user));
                maker.appendRaw(user.getDisplayName(guild) + "'s Currency balance has been incremented by: `\u200B " + getSetting(CurrencySymbolConfig.class, guild) + " " + amount + " \u200B`");
                maker.send();
                break;

            //set loot crates
            case "lootcrate":
                setSetting(LootCrateConfig.class, user, amount + getSetting(LootCrateConfig.class, user));
                maker.appendRaw(user.getDisplayName(guild) + "'s Lootcrate balance has been incremented by: `\u200B " + CRATE + " " + amount + " \u200B`");
                maker.send();
                break;

            //set the jackpot for a guild the command is used in
            case "jackpot":
                setSetting(SlotJackpotConfig.class, guild, amount + getSetting(SlotJackpotConfig.class, guild));
                maker.appendRaw(guild.getName() + "'s Jackpot balance has been incremented by: `\u200B " + getSetting(CurrencySymbolConfig.class, guild) + " " + amount + " \u200B`");
                maker.send();
                break;

            //set coffee beans
            case "beans":
                setSetting(CurrentBeansConfig.class, user, amount + getSetting(CurrentBeansConfig.class, user));
                maker.appendRaw(user.getDisplayName(guild) + "'s coffee beans have been incremented by: " + BEANS + " `\u200B " + amount + " \u200B`");
                maker.send();
                break;

            //set roasted beans
            case "roasted":
                setSetting(CurrentRoastedBeansConfig.class, user, amount + getSetting(CurrentRoastedBeansConfig.class, user));
                maker.appendRaw(user.getDisplayName(guild) + "'s roasted beans have been incremented by: " + ROASTBEANS + " `\u200B " + amount + " \u200B`");
                maker.send();
                break;

            //set coffee grounds
            case "grounds":
                setSetting(CurrentGroundsConfig.class, user, amount + getSetting(CurrentGroundsConfig.class, user));
                maker.appendRaw(user.getDisplayName(guild) + "'s coffee grounds have been incremented by: " + GROUNDS + " `\u200B " + amount + " \u200B`");
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
        return
                "#  ct <amount> // Add amount of currency to self\n" +
                        "#  ct <amount> <currency_type> // Add amount of currency_type to self OR current guild\n" +
                        "#  ct <amount> <@user> // Add amount of currency to @user\n" +
                        "#  ct <amount> <@user> <currency_type> // Add amount of currency_type to @user\n" +
                        "#  ct set <amount> // Set amount of currency for self\n" +
                        "#  ct set <amount> <currency_type> // Set amount of currency_type for self OR current guild\n" +
                        "#  ct set <amount> <@user> // Set amount of currency for @user\n" +
                        "#  ct set <amount> <@user> <currency_type> // Set amount of currency_type for @user";
    }

    //help command override description
    @Override
    public String getHelp() {

        //command description:
        return
                "#  Currency Types:\n// currency\n// lootcrate\n// jackpot\n// beans\n// roasted\n// grounds";
    }

}