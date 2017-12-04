package com.github.nija123098.evelyn.economy.plantation;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.configs.guild.GuildPrefixConfig;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.economy.configs.CurrencySymbolConfig;
import com.github.nija123098.evelyn.economy.plantation.configs.*;
import com.github.nija123098.evelyn.util.EmoticonHelper;
import com.github.nija123098.evelyn.util.FormatHelper;
import com.github.nija123098.evelyn.util.GeneralEmotes;
import com.github.nija123098.evelyn.util.TableBuilder;

import java.awt.*;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

/**
 * "The big one"
 *
 * @author Soarnir
 * @since 1.0.0
 */
public class PlantationCommand extends AbstractCommand {
    public PlantationCommand() {
        super("plantation", ModuleLevel.ECONOMY, "plant", "seedling", "check out your plantation");
    }

    @Command
    public void command(Guild guild, User user, MessageMaker maker) {

        /**
         * IMPORTANT NOTES:
         * USER LEVEL
         * CAP LEVEL 20
         * XP BASED ON USAGE
         *
         * BEAN COLLECTION FAIL
         * EVERY 10 BEANS HAVE A CHANCE OF NOT BEING COLLECTED/ROASTED
         */

        /**
         * Emoticons
         */
        final String seedling = EmoticonHelper.getChars("seedling", false);
        final String upgrades = EmoticonHelper.getChars("arrow_up", false);
        final String symbol = ConfigHandler.getSetting(CurrencySymbolConfig.class, guild);
        String home = "", homeString = GeneralEmotes.EMPTY;
        String guildPrefix = ConfigHandler.getSetting(GuildPrefixConfig.class, guild);

        /**
         * User XP and things
         */
        int userLevel = ConfigHandler.getSetting(PlantationUserLevelConfig.class, user);



        /**
         * upgrade costs and things
         */
        int maxLevel = ConfigHandler.getSetting(CurrentHouseUpgradesConfig.class, user);
        int houseUpgradeCost = Integer.valueOf(Long.toString(Math.round(Math.pow(Double.parseDouble(String.valueOf(10 + ConfigHandler.getSetting(CurrentHouseUpgradesConfig.class, user))), 3))));
        int harvestUpgradeCost = Integer.valueOf(Long.toString(Math.round(Math.pow(Double.parseDouble(String.valueOf(10 + ConfigHandler.getSetting(CurrentHarvestUpgradesConfig.class, user))), 3))));
        int roastUpgradeCost = Integer.valueOf(Long.toString(Math.round(Math.pow(Double.parseDouble(String.valueOf(10 + ConfigHandler.getSetting(CurrentRoasterUpgradesConfig.class, user))), 3))));
        int grindUpgradeCost = Integer.valueOf(Long.toString(Math.round(Math.pow(Double.parseDouble(String.valueOf(10 + ConfigHandler.getSetting(CurrentGrinderUpgradesConfig.class, user))), 3))));
        int brewUpgradeCost = Integer.valueOf(Long.toString(Math.round(Math.pow(Double.parseDouble(String.valueOf(10 + ConfigHandler.getSetting(CurrentGrinderUpgradesConfig.class, user))), 3))));
        int steeperUpgradeCost = Integer.valueOf(Long.toString(Math.round(Math.pow(Double.parseDouble(String.valueOf(10 + ConfigHandler.getSetting(CurrentSteeperUpgradesConfig.class, user))), 3))));

        /**
         * Do ye have the base upgrade?
         */
        Boolean hasHouseUpgrade = ConfigHandler.getSetting(CurrentHouseUpgradesConfig.class, user) >= 1;
        Boolean hasHarvestUpgrade = ConfigHandler.getSetting(CurrentHarvestUpgradesConfig.class, user) >= 1;
        Boolean hasRoastUpgrade = ConfigHandler.getSetting(CurrentRoasterUpgradesConfig.class, user) >= 1;
        Boolean hasGrindUpgrade = ConfigHandler.getSetting(CurrentGrinderUpgradesConfig.class, user) >= 1;
        Boolean hasBrewUpgrade = ConfigHandler.getSetting(CurrentBrewerUpgradesConfig.class, user) >= 1;
        Boolean hasSteeperUpgrade = ConfigHandler.getSetting(CurrentSteeperUpgradesConfig.class, user) >= 1;

        /**
         * Seedling rows + house
         */
        StringBuilder seedlingBuilder = new StringBuilder();
        int counter = 0;
        switch (ConfigHandler.getSetting(CurrentHouseUpgradesConfig.class, user)) {
            case 0:
                home = home + GeneralEmotes.EMPTY;
                break;
            case 1:
                home = EmoticonHelper.getChars("house_abandoned", false);
                if (hasHouseUpgrade) homeString = "house_abandoned";
                break;
            case 2:
                home = EmoticonHelper.getChars("house", false);
                if (hasHouseUpgrade) homeString = "house";
                break;
            case 3:
                home = EmoticonHelper.getChars("house_with_garden", false);
                if (hasHouseUpgrade) homeString = "house_with_garden";
                break;
        }
        seedlingBuilder.append(home);
        for (int i = 0; i < (ConfigHandler.getSetting(CurrentHarvestUpgradesConfig.class, user) * 5); i++) {
            seedlingBuilder.append(seedling);
            counter++;
            if (counter == 5) {
                seedlingBuilder.append("\n").append(GeneralEmotes.EMPTY);
                counter = 0;
            }
        }
        String plantation = seedlingBuilder.toString();

        /**
         * Plantation
         */
        maker.withColor(new Color(54,57,62)).getTitle().appendRaw(user.getDisplayName(guild) + "'s " + CoffeeEmotes.COFFEE + " Plantation");
        if (hasHarvestUpgrade || hasRoastUpgrade || hasGrindUpgrade || hasBrewUpgrade) {
            maker.getNewFieldPart().withInline(true).withBoth("Current Inventory:",
                    (((hasHarvestUpgrade) ? (CoffeeEmotes.BEANS + " `" + ConfigHandler.getSetting(CurrentBeansConfig.class, user) + "`\n") : "") +
                    ((hasRoastUpgrade) ? (CoffeeEmotes.ROASTBEANS + " `" + ConfigHandler.getSetting(CurrentRoastedBeansConfig.class, user) + "`\n") : "") +
                    ((hasGrindUpgrade) ? (CoffeeEmotes.GROUNDS + " `" + ConfigHandler.getSetting(CurrentGroundsConfig.class, user) + "`\n") : "") +
                    ((hasBrewUpgrade) ? (CoffeeEmotes.COFFEE + " `" + ConfigHandler.getSetting(HasCoffeeConfig.class, user) + "`\n") : "") +
                    ((hasSteeperUpgrade) ? (CoffeeEmotes.STEEPER+ " `" + ConfigHandler.getSetting(CurrentColdBrewConfig.class, user) + "`\n") : "")));
            maker.getNewFieldPart().withInline(true).withBoth("\u200b", plantation);
            maker.getNewFieldPart().withInline(false).withBoth("\u200b", (hasHarvestUpgrade ? (seedling + " `Harvest` | ") : "") + (hasRoastUpgrade ? (CoffeeEmotes.ROASTER + " `Roast` | ") : "") + (hasGrindUpgrade ? (CoffeeEmotes.GRINDER + " `Grind` | ") : "") + (hasBrewUpgrade ? (CoffeeEmotes.BREWER + " `Brew` | ") : "") + (hasSteeperUpgrade ? CoffeeEmotes.STEEPER + " `Steep` | " : "") + (upgrades + " `Upgrade`"));
        } else {
            maker.getNewFieldPart().withInline(true).withBoth("Welcome to your very own " + CoffeeEmotes.COFFEE + " plantation!", "it's currently quite empty, but you can tell there's good ground for building the foundations of a magnificent coffee plantation.\nYou should start by going to the " + upgrades + " upgrades tab.");
            maker.getNewFieldPart().withInline(false).withBoth("\u200b", (hasHarvestUpgrade ? (seedling + " `Harvest` | ") : "") + (hasRoastUpgrade ? (CoffeeEmotes.ROASTER + " `Roast` | ") : "") + (hasGrindUpgrade ? (CoffeeEmotes.GRINDER + " `Grind` | ") : "") + (hasBrewUpgrade ? (CoffeeEmotes.BREWER + " `Brew` | ") : "") + (hasSteeperUpgrade ? CoffeeEmotes.STEEPER + " `Steep` | " : "") + (upgrades + " `Upgrade`"));
        }

        /**
         * Return to yer plantation
         */

        if (hasHouseUpgrade) {
            maker.withReactionBehavior(homeString, ((add, reaction, u) -> {
                maker.getHeader().clear();
                maker.clearFieldParts();
                maker.getFooter().clear();
                maker.appendRaw("test");
                maker.getNewFieldPart().withInline(true).withBoth("Current Inventory:",
                        (((hasHarvestUpgrade) ? (CoffeeEmotes.BEANS + " `" + ConfigHandler.getSetting(CurrentBeansConfig.class, user) + "`\n") : "") +
                                ((hasRoastUpgrade) ? (CoffeeEmotes.ROASTBEANS + " `" + ConfigHandler.getSetting(CurrentRoastedBeansConfig.class, user) + "`\n") : "") +
                                ((hasGrindUpgrade) ? (CoffeeEmotes.GROUNDS + " `" + ConfigHandler.getSetting(CurrentGroundsConfig.class, user) + "`\n") : "") +
                                ((hasBrewUpgrade) ? (CoffeeEmotes.COFFEE + " `" + ConfigHandler.getSetting(HasCoffeeConfig.class, user) + "`\n") : "") +
                                ((hasSteeperUpgrade) ? (CoffeeEmotes.STEEPER+ " `" + ConfigHandler.getSetting(CurrentColdBrewConfig.class, user) + "`\n") : "")));
                maker.getNewFieldPart().withInline(true).withBoth("\u200b", plantation);
                maker.getNewFieldPart().withInline(false).withBoth("\u200b", (hasHarvestUpgrade ? (seedling + " `Harvest` | ") : "") + (hasRoastUpgrade ? (CoffeeEmotes.ROASTER + " `Roast` | ") : "") + (hasGrindUpgrade ? (CoffeeEmotes.GRINDER + " `Grind` | ") : "") + (hasBrewUpgrade ? (CoffeeEmotes.BREWER + " `Brew` | ") : "") + (hasSteeperUpgrade ? CoffeeEmotes.STEEPER + " `Steep` | " : "") + (upgrades + " `Upgrade`"));
                maker.forceCompile().send();
            }));
        }

        /**
         * Collect yer beans
         */
        if (hasHarvestUpgrade) {
            maker.withReactionBehavior("seedling", ((add, reaction, u) -> {
                maker.forceCompile().getHeader().clear();
                maker.forceCompile().clearFieldParts();
                maker.forceCompile().getFooter().clear();


                /**
                 * Time be a fickle thing
                 */
                Instant then = Instant.parse(ConfigHandler.getSetting(LastHarvestUseConfig.class, user)), thenDays = then.truncatedTo(ChronoUnit.DAYS), thenDaysCount = then.truncatedTo(ChronoUnit.DAYS);
                Instant now = Clock.systemUTC().instant(), nowDays = now.truncatedTo(ChronoUnit.DAYS), nowDaysCount = now.truncatedTo(ChronoUnit.DAYS);
                Instant utcMidnight = Instant.parse((ZonedDateTime.now(ZoneId.of("Z")).plusDays(1)).toString());
                int timeUntil = Math.abs(Integer.valueOf(String.valueOf(now.until(utcMidnight.atZone(ZoneId.of("Z")).truncatedTo(ChronoUnit.DAYS), ChronoUnit.MINUTES))));
                int hours = 0, minutes;
                while (timeUntil >= 60) {
                    hours++;
                    timeUntil = timeUntil - 60;
                }
                minutes = timeUntil;

                int beanClaim = (100 * ConfigHandler.getSetting(CurrentHarvestUpgradesConfig.class, user));
                int currentBeans = ConfigHandler.getSetting(CurrentBeansConfig.class, user);
                if (nowDays.compareTo(thenDays) == 1) {
                    ConfigHandler.setSetting(CurrentBeansConfig.class, user, (currentBeans + beanClaim));
                    maker.appendRaw("You collected " + CoffeeEmotes.BEANS + " " + beanClaim + " from your plantation");
                    ConfigHandler.setSetting(LastHarvestUseConfig.class, user, now.toString());
                } else {
                    maker.appendRaw("You'll need to wait: " + hours + "h and " + minutes + "m to harvest from your plantation");
                }
                maker.getNewFieldPart().withInline(true).withBoth("Current Inventory:",
                        ((CoffeeEmotes.BEANS + " `" + ((nowDays.compareTo(thenDays) == 1) ? (currentBeans + beanClaim) : currentBeans) + "`\n") +
                        ((hasRoastUpgrade) ? (CoffeeEmotes.ROASTBEANS + " `" + ConfigHandler.getSetting(CurrentRoastedBeansConfig.class, user) + "`\n") : "") +
                        ((hasGrindUpgrade) ? (CoffeeEmotes.GROUNDS + " `" + ConfigHandler.getSetting(CurrentGroundsConfig.class, user) + "`\n") : "") +
                        ((hasBrewUpgrade) ? (CoffeeEmotes.COFFEE + " `" + ConfigHandler.getSetting(HasCoffeeConfig.class, user) + "`\n") : "") +
                        ((hasSteeperUpgrade) ? (CoffeeEmotes.STEEPER+ " `" + ConfigHandler.getSetting(CurrentColdBrewConfig.class, user) + "`\n") : "")));
                maker.getNewFieldPart().withInline(true).withBoth("\u200b",plantation);
                maker.getNewFieldPart().withInline(false).withBoth("\u200b", seedling + FormatHelper.embedLink(" `Harvest`", "") + " | " + (hasRoastUpgrade ? CoffeeEmotes.ROASTER + " `Roast` | " : "") + (hasGrindUpgrade ? CoffeeEmotes.GRINDER + " `Grind` | " : "") + (hasBrewUpgrade ? CoffeeEmotes.BREWER + " `Brew` | " : "") + (hasSteeperUpgrade ? CoffeeEmotes.STEEPER + " `Steep` | " : "") + upgrades + " `Upgrade`");
                maker.forceCompile().send();
            }));
        }

        /**
         * Roast yer beans
         */
        if (hasRoastUpgrade) {
            maker.withReactionBehavior("roaster", ((add, reaction, u) -> {
                maker.forceCompile().getHeader().clear();
                maker.clearFieldParts();
                maker.forceCompile().getFooter().clear();
                //Add code here
                int totalBeans = ConfigHandler.getSetting(CurrentBeansConfig.class, user);
                int totalRoastBeans = ConfigHandler.getSetting(CurrentRoastedBeansConfig.class, user);
                if (totalBeans < 1) {
                    maker.appendRaw("You don't have enough " + CoffeeEmotes.BEANS + " to roast");
                } else {
                    ConfigHandler.setSetting(CurrentBeansConfig.class, user, 0);
                    ConfigHandler.setSetting(CurrentRoastedBeansConfig.class, user, totalRoastBeans + totalBeans);
                    maker.appendRaw("You roasted `" + totalBeans + "` " + CoffeeEmotes.BEANS);
                }
                maker.getNewFieldPart().withInline(true).withBoth("Current Inventory:",
                        (((hasHarvestUpgrade) ? (CoffeeEmotes.BEANS + " `" + (totalBeans < 1 ? totalBeans : 0) + "`\n") : "") +
                        (CoffeeEmotes.ROASTBEANS + " `" + (totalBeans < 1 ? totalRoastBeans : (totalRoastBeans + totalBeans)) + "`\n") +
                        ((hasGrindUpgrade) ? (CoffeeEmotes.GROUNDS + " `" + ConfigHandler.getSetting(CurrentGroundsConfig.class, user) + "`\n") : "") +
                        ((hasBrewUpgrade) ? (CoffeeEmotes.COFFEE + " `" + ConfigHandler.getSetting(HasCoffeeConfig.class, user) + "`\n") : "") +
                        ((hasSteeperUpgrade) ? (CoffeeEmotes.STEEPER+ " `" + ConfigHandler.getSetting(CurrentColdBrewConfig.class, user) + "`\n") : "")));
                maker.getNewFieldPart().withInline(true).withBoth("\u200b", plantation);
                maker.getNewFieldPart().withInline(false).withBoth("\u200b", (hasHarvestUpgrade ? seedling + " `Harvest` | " : "") + CoffeeEmotes.ROASTER + FormatHelper.embedLink(" `Roast`", "") + " | " + (hasGrindUpgrade ? CoffeeEmotes.GRINDER + " `Grind` | " : "") + (hasBrewUpgrade ? CoffeeEmotes.BREWER + " `Brew` | " : "") + (hasSteeperUpgrade ? CoffeeEmotes.STEEPER + " `Steep` | " : "") + upgrades + " `Upgrade`");
                maker.forceCompile().send();
            }));
        }

        /**
         * Grind yer roasted beans
         */
        if (hasGrindUpgrade) {
            maker.withReactionBehavior("grinder", ((add, reaction, u) -> {
                maker.forceCompile().getHeader().clear();
                maker.forceCompile().clearFieldParts();
                maker.forceCompile().getFooter().clear();
                //Add code here
                int previous = ConfigHandler.getSetting(CurrentRoastedBeansConfig.class, user);
                int total = ConfigHandler.getSetting(CurrentRoastedBeansConfig.class, user), count = 0;
                int currentGrounds = ConfigHandler.getSetting(CurrentGroundsConfig.class, user);
                if (total <= 15) {
                    maker.appendRaw("You don't have enough roasted " + CoffeeEmotes.ROASTBEANS + " to make " + CoffeeEmotes.GROUNDS + "\nYou need " + (16 - total) + " more roasted beans to make " + CoffeeEmotes.GROUNDS + ".");
                } else {
                    while (total >= 16) {
                        count++;
                        total = total - 16;
                    }
                    ConfigHandler.setSetting(CurrentRoastedBeansConfig.class, user, total);
                    ConfigHandler.setSetting(CurrentGroundsConfig.class, user, (count + currentGrounds));
                    maker.appendRaw("You ground " + previous + " " + CoffeeEmotes.ROASTBEANS + " into " + count + " coffee " + CoffeeEmotes.GROUNDS);
                }
                maker.getNewFieldPart().withInline(true).withBoth("Current Inventory:",
                        (((hasHarvestUpgrade) ? (CoffeeEmotes.BEANS + " `" + ConfigHandler.getSetting(CurrentBeansConfig.class, user) + "`\n") : "") +
                        ((hasRoastUpgrade) ? (CoffeeEmotes.ROASTBEANS + " `" + (previous <= 15 ? previous : total ) + "`\n") : "") +
                        (CoffeeEmotes.GROUNDS + " `" + (previous <= 15 ? currentGrounds : (count + currentGrounds)) + "`\n") +
                        ((hasBrewUpgrade) ? (CoffeeEmotes.COFFEE + " `" + ConfigHandler.getSetting(HasCoffeeConfig.class, user) + "`\n") : "") +
                        ((hasSteeperUpgrade) ? (CoffeeEmotes.STEEPER+ " `" + ConfigHandler.getSetting(CurrentColdBrewConfig.class, user) + "`\n") : "")));
                maker.getNewFieldPart().withInline(true).withBoth("\u200b", plantation);
                maker.getNewFieldPart().withInline(false).withBoth("\u200b", (hasHarvestUpgrade ? seedling + " `Harvest` | " : "") + (hasRoastUpgrade ? CoffeeEmotes.ROASTER + " `Roast` | " : "") + CoffeeEmotes.GRINDER + FormatHelper.embedLink(" `Grind`", "") + " | " + (hasBrewUpgrade ? CoffeeEmotes.BREWER + " `Brew` | " : "") + (hasSteeperUpgrade ? CoffeeEmotes.STEEPER + " `Steep` | " : "") + upgrades + " `Upgrade`");
                maker.forceCompile().send();
            }));
        }

        /**
         * Brew some coffee out of the roasted beans you ground earlier
         */
        if (hasBrewUpgrade) {
            maker.withReactionBehavior("brewer", ((add, reaction, u) -> {
                maker.forceCompile().getHeader().clear();
                maker.forceCompile().clearFieldParts();
                maker.forceCompile().getFooter().clear();
                //add code here
                int totalGrounds = ConfigHandler.getSetting(CurrentGroundsConfig.class, user);
                int brewUpgrade = ConfigHandler.getSetting(CurrentBrewerUpgradesConfig.class, user);
                boolean hasCoffee = ConfigHandler.getSetting(HasCoffeeConfig.class, user);
                if (hasCoffee) {
                    maker.appendRaw("You already have a cup of " + CoffeeEmotes.COFFEE + ", you wouldn't let it go to waste would you?\nYou should try to `" + guildPrefix + "drink` it");
                } else if (totalGrounds < 4) {
                    maker.appendRaw("You do not have enough coffee " + CoffeeEmotes.GROUNDS + " to make " + CoffeeEmotes.COFFEE + "\nYou need " + (4 - totalGrounds) + " more " + CoffeeEmotes.GROUNDS + " to brew some " + CoffeeEmotes.COFFEE);
                } else {
                    switch (brewUpgrade) {
                        case 1:
                            maker.appendRaw("You brew a cup of bitter " + CoffeeEmotes.COFFEE + ", you're fairly certain it is coffee, but you're not sure.");
                            break;
                        case 2:
                            maker.appendRaw("You brew a cup of " + CoffeeEmotes.COFFEE + ", it's not great, but at least it tastes like coffee");
                            break;
                        case 3:
                            maker.appendRaw("You brew a cup of good " + CoffeeEmotes.COFFEE + ", you'd gladly sell this knowing the quality is excellent");
                            break;
                        case 4:
                            maker.appendRaw("You brew a cup of delicious " + CoffeeEmotes.COFFEE + ", you think it just might be the best cup you've ever brewed.");
                            break;
                    }
                    ConfigHandler.setSetting(HasCoffeeConfig.class, user, true);
                    ConfigHandler.setSetting(CoffeeBrewedConfig.class, user, Clock.systemUTC().instant().toString());
                    ConfigHandler.setSetting(CurrentGroundsConfig.class, user, totalGrounds - 4);
                }
                maker.getNewFieldPart().withInline(true).withBoth("Current Inventory:",
                        (((hasHarvestUpgrade) ? (CoffeeEmotes.BEANS + " `" + ConfigHandler.getSetting(CurrentBeansConfig.class, user) + "`\n") : "") +
                        ((hasRoastUpgrade) ? (CoffeeEmotes.ROASTBEANS + " `" + ConfigHandler.getSetting(CurrentRoastedBeansConfig.class, user) + "`\n") : "") +
                        ((hasGrindUpgrade) ? (CoffeeEmotes.GROUNDS + " `" + ((!hasCoffee && totalGrounds >= 4) ? (totalGrounds - 4) : totalGrounds) + "`\n") : "") +
                        (CoffeeEmotes.COFFEE + " `" + ((!hasCoffee && totalGrounds >= 4) ? String.valueOf(true) : String.valueOf(false)) + "`\n") +
                        ((hasSteeperUpgrade) ? (CoffeeEmotes.STEEPER+ " `" + ConfigHandler.getSetting(CurrentColdBrewConfig.class, user) + "`\n") : "")));
                maker.getNewFieldPart().withInline(true).withBoth("\u200b", plantation);
                maker.getNewFieldPart().withInline(false).withBoth("\u200b", (hasHarvestUpgrade ? (seedling + " `Harvest` | ") : "") + (hasRoastUpgrade ? (CoffeeEmotes.ROASTER + " `Roast` | ") : "") + (hasGrindUpgrade ? (CoffeeEmotes.GRINDER + " `Grind` | ") : "") + (CoffeeEmotes.BREWER + FormatHelper.embedLink(" `Brew` ", "") + "| ") + (hasSteeperUpgrade ? CoffeeEmotes.STEEPER + " `Steep` | " : "") + (upgrades + " `Upgrade`"));
                maker.forceCompile().send();
            }));
        }

        /**
         * Can't have more than one coffee? Bullshit, make some cold brew
         */
        if (hasSteeperUpgrade) {
            maker.withReactionBehavior("steeper", (add, reaction, u) -> {
                maker.forceCompile().getHeader().clear();
                maker.forceCompile().clearFieldParts();
                maker.forceCompile().getFooter().clear();
                //add code here
                /**
                 * Time be a fickle thing
                 */
                Instant then;
                try {
                    then = Instant.parse(ConfigHandler.getSetting(SteepedCoffeeBrewedConfig.class, user)).truncatedTo(ChronoUnit.SECONDS).plus(1, ChronoUnit.DAYS);
                } catch (DateTimeParseException e) {
                    then = Instant.now();
                }
                Instant now = Clock.systemUTC().instant().truncatedTo(ChronoUnit.SECONDS);
                int timeUntil = Math.abs(Integer.valueOf(String.valueOf(now.until(then.atZone(ZoneId.of("Z")), ChronoUnit.MINUTES))));
                int currentColdBrew = ConfigHandler.getSetting(CurrentColdBrewConfig.class, user), currentSteepedBatch = ConfigHandler.getSetting(CurrentSteepedCoffeeBatchConfig.class, user);
                int batchGrounds = 0, previous = ConfigHandler.getSetting(CurrentGroundsConfig.class, user);
                boolean batchFinished = false, batchMade = false;
                if ((ConfigHandler.getSetting(CurrentSteepedCoffeeBatchConfig.class, user) >= 1) && now.compareTo(then) == -1) {
                    int hours = 0, minutes;
                    while (timeUntil >= 60) {
                        hours++;
                        timeUntil -= 60;
                    }
                    minutes = timeUntil;
                    maker.appendRaw("you're already steeping a batch of cold brew coffee, wait " + hours + "h and " + minutes + "m until your cold brew is finished");
                } else if ((ConfigHandler.getSetting(CurrentSteepedCoffeeBatchConfig.class, user) >= 1) && now.compareTo(then) > -1){
                    maker.appendRaw("Your cold brew batch has finished steeping!\n" + "You made " + currentSteepedBatch + " cold brews!");
                    ConfigHandler.setSetting(CurrentSteepedCoffeeBatchConfig.class, user, 0);
                    ConfigHandler.setSetting(CurrentColdBrewConfig.class, user, (currentColdBrew + currentSteepedBatch));
                    batchFinished = true;
                } else {
                    int currentGrounds = ConfigHandler.getSetting(CurrentGroundsConfig.class, user);
                    int counterGrounds = currentGrounds;
                    int steepedBatch = 0;
                    while (counterGrounds >= 8) {
                        steepedBatch++;
                        counterGrounds -= 8;
                    }
                    if (currentGrounds >= (steepedBatch * 8) && steepedBatch < 0) {
                        maker.getFooter().appendRaw("Created a new batch of " + steepedBatch + " cold brews which should be finished in 24h");
                        batchMade = true;
                        batchGrounds = counterGrounds;
                        ConfigHandler.setSetting(CurrentSteepedCoffeeBatchConfig.class, user, steepedBatch);
                        ConfigHandler.setSetting(CurrentGroundsConfig.class, user, counterGrounds);
                        ConfigHandler.setSetting(SteepedCoffeeBrewedConfig.class, user, Clock.systemUTC().instant().toString());
                    } else {
                        if (steepedBatch == 0) {
                            steepedBatch = 1;
                        }
                        maker.getFooter().appendRaw("You don't have enough " + CoffeeEmotes.GROUNDS + " to make a batch of cold brew, you need " + ((steepedBatch * 8) - currentGrounds) + " more " + CoffeeEmotes.GROUNDS + " to make one batch of " + steepedBatch + " cold brews");
                    }
                }
                maker.getNewFieldPart().withInline(true).withBoth("Current Inventory:",
                        (((hasHarvestUpgrade) ? (CoffeeEmotes.BEANS + " `" + ConfigHandler.getSetting(CurrentBeansConfig.class, user) + "`\n") : "") +
                        ((hasRoastUpgrade) ? (CoffeeEmotes.ROASTBEANS + " `" + ConfigHandler.getSetting(CurrentRoastedBeansConfig.class, user) + "`\n") : "") +
                        ((hasGrindUpgrade) ? (CoffeeEmotes.GROUNDS + " `" + (batchMade ? batchGrounds : previous) + "`\n") : "") +
                        ((hasBrewUpgrade) ? (CoffeeEmotes.COFFEE + " `" + ConfigHandler.getSetting(HasCoffeeConfig.class, user) + "`\n") : "") +
                        (CoffeeEmotes.STEEPER+ " `" + (batchFinished ? (currentColdBrew + currentSteepedBatch) : currentColdBrew) + "`\n")));
                maker.getNewFieldPart().withInline(true).withBoth("\u200b", plantation);
                maker.getNewFieldPart().withInline(false).withBoth("\u200b", (hasHarvestUpgrade ? seedling + " `Harvest` | " : "") + (hasRoastUpgrade ? CoffeeEmotes.ROASTER + " `Roast` | " : "") + CoffeeEmotes.GRINDER + " `Grind` | " + (hasBrewUpgrade ? CoffeeEmotes.BREWER + " `Brew` | " : "") + CoffeeEmotes.STEEPER + FormatHelper.embedLink(" `Steep` ", "") + "| " + upgrades + " `Upgrade`");
                maker.forceCompile().send();
            });
        }

        /**
         * You want to do anything with yer plantation do ye? start here, pleb.
         */
        maker.withReactionBehavior("arrow_up", (add, reaction, u) -> {
            maker.forceCompile().getHeader().clear();
            maker.forceCompile().clearFieldParts();
            maker.forceCompile().getFooter().clear();

            int currentHarvestUpgrades = ConfigHandler.getSetting(CurrentHarvestUpgradesConfig.class, user);
            int currentHouseUpgrades = ConfigHandler.getSetting(CurrentHouseUpgradesConfig.class, user);
            int currentRoastUpgrades = ConfigHandler.getSetting(CurrentRoasterUpgradesConfig.class, user);
            int currentGrindUpgrades = ConfigHandler.getSetting(CurrentGrinderUpgradesConfig.class, user);
            int currentBrewUpgrades = ConfigHandler.getSetting(CurrentBrewerUpgradesConfig.class, user);
            int currentSteeperUpgrades = ConfigHandler.getSetting(CurrentSteeperUpgradesConfig.class, user);

            TableBuilder tb = new TableBuilder();
            tb.addRow("**Facility**", "**Current level**", "**Upgrade cost**");
            tb.addRow("House", (((currentHouseUpgrades == 3) ? "max level" : String.valueOf(currentHouseUpgrades))), ((currentHouseUpgrades == 3) ? "N/A" : (symbol + " " + houseUpgradeCost)));
            tb.addRow("Plants", (((currentHarvestUpgrades == 4) ? "max level" : String.valueOf(currentHarvestUpgrades))), ((currentHarvestUpgrades == 4) ? "N/A" : (symbol + " " + harvestUpgradeCost)));
            tb.addRow("Roaster", (((currentRoastUpgrades == maxLevel) ?  "max level" : String.valueOf(currentRoastUpgrades))), ((currentRoastUpgrades == maxLevel) ? "N/A" : (symbol + " " + roastUpgradeCost)));
            tb.addRow("Grinder", (((currentGrindUpgrades == maxLevel) ?  "max level" : String.valueOf(currentGrindUpgrades))), ((currentGrindUpgrades == maxLevel) ? "N/A" : (symbol + " " + grindUpgradeCost)));
            tb.addRow("Brewer", (((currentBrewUpgrades == maxLevel) ? "max level" : String.valueOf(currentBrewUpgrades))), ((currentBrewUpgrades == maxLevel) ? "N/A" : (symbol + " " + brewUpgradeCost)));
            tb.addRow("Steeper", (((currentSteeperUpgrades == maxLevel) ? "max level" : String.valueOf(currentSteeperUpgrades))), ((currentSteeperUpgrades == maxLevel) ? "N/A" : (symbol + " " + steeperUpgradeCost)));
            maker.getNewFieldPart().withBoth("Current Upgrades:", tb.toString());
            if ((currentHouseUpgrades >= 0 && currentHouseUpgrades < 3) && ((currentRoastUpgrades == maxLevel) && (currentGrindUpgrades == maxLevel) && (currentBrewUpgrades == maxLevel) && (currentSteeperUpgrades == maxLevel))) {
                maker.getFooter().appendRaw("*It looks like all of your facilities are limited by the current level of the house, upgrade the house to unlock more powerful facilities.\nThe coffee plants upgrade independently and are not limited by its upgrade*");
            }
            maker.getNewFieldPart().withBoth("\u200b", "use `" + guildPrefix + "upgrade <facility name>` to upgrade your plantation");
            maker.getNewFieldPart().withInline(false).withBoth("\u200b", (hasHarvestUpgrade ? (seedling + " `Harvest` | ") : "") + (hasRoastUpgrade ? (CoffeeEmotes.ROASTER + " `Roast` | ") : "") + (hasGrindUpgrade ? (CoffeeEmotes.GRINDER + " `Grind` | ") : "") + (hasBrewUpgrade ? (CoffeeEmotes.BREWER + " `Brew` | ") : "") + (hasSteeperUpgrade ? CoffeeEmotes.STEEPER + " `Steep` | " : "") + (upgrades + FormatHelper.embedLink(" `Upgrade`", "")));
            maker.forceCompile().send();
        });
    }
}
