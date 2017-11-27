package com.github.nija123098.evelyn.economy;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.configs.guild.GuildPrefixConfig;
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
import com.github.nija123098.evelyn.util.EmoticonHelper;

import java.awt.*;

/**
 * Written by Dxeo 26/11/2017
 */

public class InventoryCommand extends AbstractCommand {

    //constructor
    public InventoryCommand() {
        super("inventory", ModuleLevel.ECONOMY, "inv", null, "open your inventory");
    }

    @Command
    public void command(Guild guild, User user, MessageMaker maker){

        //configure maker
        maker.withAutoSend(false);
        maker.mustEmbed();
        maker.withColor(new Color(54,57,62));

        //construct inventory to send
        maker.getTitle().clear().appendRaw(EmoticonHelper.getChars("school_satchel",false) + " " + user.getDisplayName(guild) + "'s inventory");
        maker.getHeader().clear().appendRaw("\u200B\nFunds: `\u200B " + ConfigHandler.getSetting(CurrencySymbolConfig.class, guild) + " " + ConfigHandler.getSetting(CurrentCurrencyConfig.class, user) + " \u200B`");
        maker.getNote().clear().appendRaw("If you'd like to know more about a certain section use " + ConfigHandler.getExteriorSetting(GuildPrefixConfig.class, guild) + "help <section_name>");

        //lootcrate section
        maker.getNewFieldPart().withBoth("Lootcrate", "`\u200B " +
                LootCrateEmotes.CRATE + " " +
                ConfigHandler.getSetting(LootCrateConfig.class, user) +
                " \u200B`");

        //plantation section
        maker.getNewFieldPart().withBoth("Plantation",(
                CoffeeEmotes.BEANS + " `" + ConfigHandler.getSetting(CurrentBeansConfig.class, user) + "`\n" +
                CoffeeEmotes.ROASTBEANS + " `" + ConfigHandler.getSetting(CurrentRoastedBeansConfig.class, user) + "`\n" +
                CoffeeEmotes.GROUNDS + " `" + ConfigHandler.getSetting(CurrentGroundsConfig.class, user) + "`"));
        maker.send();

    }

}