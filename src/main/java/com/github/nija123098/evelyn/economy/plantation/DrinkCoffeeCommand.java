package com.github.nija123098.evelyn.economy.plantation;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.configs.guild.GuildPrefixConfig;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.economy.plantation.configs.CoffeeBrewedConfig;
import com.github.nija123098.evelyn.economy.plantation.configs.CoffeeEmotes;
import com.github.nija123098.evelyn.economy.plantation.configs.CurrentBrewerUpgradesConfig;
import com.github.nija123098.evelyn.economy.plantation.configs.HasCoffeeConfig;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class DrinkCoffeeCommand extends AbstractCommand {
    public DrinkCoffeeCommand() {
        super("drink", null, null, "coffee", "drink your coffee");
    }

    @Command
    public void command(Guild guild, User user, MessageMaker maker) {
        String coffeequality = "";
        int brewUpgrade = ConfigHandler.getSetting(CurrentBrewerUpgradesConfig.class, user);
        maker.mustEmbed();
        if (ConfigHandler.getSetting(HasCoffeeConfig.class, user)) {
            switch (brewUpgrade) {
                case 1:
                    coffeequality = "pretty terrible, you clearly need better machinery to make anything better.";
                    break;
                case 2:
                    coffeequality = "not bad, but not great, it's got a ways to go.";
                    break;
                case 3:
                    coffeequality = "quite good, you'd be willing to sell this in a store.";
                    break;
                case 4:
                    coffeequality = "absolutely delicious, this could possibly be the best coffee around.";
                    break;
            }

            /**
             * Time be a fickle thing
             */
            Instant then = Instant.parse(ConfigHandler.getSetting(CoffeeBrewedConfig.class, user));
            Instant now = Clock.systemUTC().instant();

            int timeUntil = Math.abs(Integer.valueOf(String.valueOf(now.until(then.atZone(ZoneId.of("Z")), ChronoUnit.HOURS))));
            if (timeUntil >= 8) {
                maker.appendRaw("you've left your " + CoffeeEmotes.COFFEE + " waiting for more than 8 hours, it's now soured up due to the lack of attention, no one wants to drink sour coffee");
                ConfigHandler.setSetting(HasCoffeeConfig.class, user, false);
            } else {
                maker.appendRaw("You sit down and sip from your own freshly brewed " + CoffeeEmotes.COFFEE + ", it is " + coffeequality);
                ConfigHandler.setSetting(HasCoffeeConfig.class, user, false);
            }
        } else {
            maker.appendRaw("You stare blankly out your window and sigh as you realise you don't currently have a " + CoffeeEmotes.COFFEE + " to drink.\nYou should probably check your `" + ConfigHandler.getSetting(GuildPrefixConfig.class, guild) + "plantation`.");
        }
    }
}
