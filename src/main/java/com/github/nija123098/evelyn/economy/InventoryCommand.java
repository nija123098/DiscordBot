package com.github.nija123098.evelyn.economy;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.util.EmoticonHelper;

/**
 * @author Dxeo
 * @since 1.0.0
 */
public class InventoryCommand extends AbstractCommand {

    public InventoryCommand() {
        super("inventory", ModuleLevel.BOT_ADMINISTRATIVE, "inv", null, "open your inventory");
    }

    @Command
    public void command(Guild guild, User user, MessageMaker maker) {
        maker.appendRaw(EmoticonHelper.getChars("eyes", false));
        /*
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
                CoffeeEmotes.BEANS + " `\u200B " + ConfigHandler.getSetting(CurrentBeansConfig.class, user) + " \u200B`\n" +
                CoffeeEmotes.ROASTBEANS + " `\u200B " + ConfigHandler.getSetting(CurrentRoastedBeansConfig.class, user) + " \u200B`\n" +
                CoffeeEmotes.GROUNDS + " `\u200B " + ConfigHandler.getSetting(CurrentGroundsConfig.class, user) + " \u200B`"));
        maker.send();*/

    }
}