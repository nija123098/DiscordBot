package com.github.nija123098.evelyn.economy.plantation;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.economy.plantation.configs.CoffeeBrewedConfig;
import com.github.nija123098.evelyn.economy.plantation.configs.CoffeeEmotes;
import com.github.nija123098.evelyn.economy.plantation.configs.CurrentBrewerUpgradesConfig;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class DrinkCoffeeCommand extends AbstractCommand {
    public DrinkCoffeeCommand() {
        super("drink", ModuleLevel.FUN, null, "coffee", "drink your coffee");
    }

    @Command
    public void command(Guild guild, User user, MessageMaker maker) {
        String coffeeQuality = "tasting like nothing.";
        maker.mustEmbed();
        if (ConfigHandler.getSetting(CoffeeBrewedConfig.class, user) != 0) {
            switch (ConfigHandler.getSetting(CurrentBrewerUpgradesConfig.class, user)) {
                case 1:
                    coffeeQuality = "pretty terrible, you clearly need better machinery to make anything better.";
                    break;
                case 2:
                    coffeeQuality = "not bad, but not great, it's got a ways to go.";
                    break;
                case 3:
                    coffeeQuality = "quite good, you'd be willing to sell this in a store.";
                    break;
                case 4:
                    coffeeQuality = "absolutely delicious, this could possibly be the best coffee around.";
                    break;
            }

            // Time be a fickle thing
            if (System.currentTimeMillis() - ConfigHandler.getSetting(CoffeeBrewedConfig.class, user) >= 28_800_000) {// 8 hours
                maker.append("you've left your " + CoffeeEmotes.COFFEE + " waiting for more than 8 hours, it's now soured up due to the lack of attention, no one wants to drink sour coffee");
                ConfigHandler.setSetting(CoffeeBrewedConfig.class, user, 0L);
            } else {
                maker.append("You sit down and sip from your own freshly brewed " + CoffeeEmotes.COFFEE + ", it is " + coffeeQuality);
                ConfigHandler.setSetting(CoffeeBrewedConfig.class, user, 0L);
            }
        } else {
            maker.append("You stare meaninglessly out your window and sigh as you realise you don't currently have a " + CoffeeEmotes.COFFEE + " to drink.\nYou think about the prospects of brewing your own coffee and sigh contentedly, a problem for another day.");
            //"\nYou should probably check your `" + ConfigHandler.getSetting(GuildPrefixConfig.class, guild) + "plantation`.");
        }
    }
}
