package com.github.nija123098.evelyn.economy.plantation;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.economy.configs.CurrencySymbolConfig;
import com.github.nija123098.evelyn.economy.configs.CurrentCurrencyConfig;
import com.github.nija123098.evelyn.economy.plantation.configs.*;
import com.github.nija123098.evelyn.util.EmoticonHelper;
import com.github.nija123098.evelyn.util.FormatHelper;

import java.util.ConcurrentModificationException;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class UpgradePlantationCommand extends AbstractCommand {
    public UpgradePlantationCommand() {
        super(PlantationCommand.class, "upgrade", "upgrade", null, null, "upgrade your plantation");
    }

    @Command
    public void command(@Argument String arg, User user, Guild guild, MessageMaker maker) {
        String symbol = ConfigHandler.getSetting(CurrencySymbolConfig.class, guild);

        int currency = ConfigHandler.getSetting(CurrentCurrencyConfig.class, user);
        int maxLevel = ConfigHandler.getSetting(CurrentHouseUpgradesConfig.class, user);

        switch (arg.toLowerCase()) {
            case "house":
                maker.getTitle().appendRaw("House");
                switch (ConfigHandler.getSetting(CurrentHouseUpgradesConfig.class, user)) {
                    case 0:
                        maker.withThumb(EmoticonHelper.getEmoji("EMPTY").getImageUrl());
                        break;
                    case 1:
                        maker.withThumb("https://cdn.discordapp.com/attachments/374538747229896704/385399923329466368/house_abandoned.png");
                        break;
                    case 2:
                        maker.withThumb("https://cdn.discordapp.com/attachments/374538747229896704/385399931092860928/house.png");
                        break;
                    case 3:
                        maker.withThumb("https://cdn.discordapp.com/attachments/374538747229896704/385399928249122816/house_with_garden.png");
                        break;
                }
                if (ConfigHandler.getSetting(CurrentHouseUpgradesConfig.class, user) == 3) {
                    maker.appendRaw("You've reached the maximum house level");
                } else {
                    int houseUpgradeCost = Integer.valueOf(Long.toString(Math.round(Math.pow(Double.parseDouble(String.valueOf(10 + ConfigHandler.getSetting(CurrentHouseUpgradesConfig.class, user))), 3))));
                    maker.appendRaw("\nCurrent level: ").appendEmbedLink(String.valueOf(ConfigHandler.getSetting(CurrentHouseUpgradesConfig.class, user)), "");
                    maker.appendRaw("\nUpgrade cost: `\u200b " + symbol + " " +  houseUpgradeCost + " \u200b`");
                    maker.appendRaw("\nCurrent Funds: `\u200b " + symbol + " " +  currency + " \u200b`");
                    maker.withReactionBehavior("red_tick", ((add, reaction, u) -> {
                        //remove reactions
                        try {
                            maker.clearReactionBehaviors();
                        } catch (ConcurrentModificationException IGNORE) {
                        }
                        maker.sentMessage().getReactions().forEach(reactions -> maker.sentMessage().removeReaction(reactions));

                    }));
                    maker.withReactionBehavior("green_tick", ((add, reaction, u) -> {

                        //remove reactions
                        try {
                            maker.clearReactionBehaviors();
                        } catch (ConcurrentModificationException IGNORE) {
                        }
                        maker.sentMessage().getReactions().forEach(reactions -> maker.sentMessage().removeReaction(reactions));

                        maker.getHeader().clear();
                        int currencyTemp = currency;
                        int houseUpgrades = ConfigHandler.getSetting(CurrentHouseUpgradesConfig.class, user);

                        if (currencyTemp < houseUpgradeCost) {
                            maker.appendRaw("You need `\u200b " + symbol + " " + (houseUpgradeCost - currencyTemp) + " \u200b` more to buy this upgrade");
                        } else {
                            switch (houseUpgrades) {
                                case 0:
                                    maker.withThumb("https://cdn.discordapp.com/attachments/374538747229896704/385399923329466368/house_abandoned.png");
                                    break;
                                case 1:
                                    maker.withThumb("https://cdn.discordapp.com/attachments/374538747229896704/385399931092860928/house.png");
                                    break;
                                case 2:
                                    maker.withThumb("https://cdn.discordapp.com/attachments/374538747229896704/385399928249122816/house_with_garden.png");
                                    break;
                            }
                            maker.appendRaw("Now level: ").appendEmbedLink(String.valueOf(++houseUpgrades), "").appendRaw("\n\nFunds: `\u200b " + symbol + " " + (currencyTemp - houseUpgradeCost) + " \u200b`");
                            ConfigHandler.setSetting(CurrentCurrencyConfig.class, user, (currencyTemp - houseUpgradeCost));
                            ConfigHandler.setSetting(CurrentHouseUpgradesConfig.class, user, houseUpgrades);
                        }
                        maker.forceCompile().withoutReactionBehavior("green_tick").send();
                        maker.forceCompile().send();
                    }));
                }
                break;

            case "plants":
            case "plant":
                maker.getTitle().appendRaw("Plants");
                maker.withThumb("https://cdn.discordapp.com/attachments/374538747229896704/385399941830541314/seedling.png");
                if (ConfigHandler.getSetting(CurrentHarvestUpgradesConfig.class, user) == 4) {
                    maker.appendRaw("You've reached the maximum field size for your plantation, you can't fit any more coffee plants here");
                } else {
                    int harvestUpgradeCost = Integer.valueOf(Long.toString(Math.round(Math.pow(Double.parseDouble(String.valueOf(10 + ConfigHandler.getSetting(CurrentHarvestUpgradesConfig.class, user))), 3))));
                    maker.appendRaw("\nCurrent level: ").appendEmbedLink(String.valueOf(ConfigHandler.getSetting(CurrentHarvestUpgradesConfig.class, user)), "");
                    maker.appendRaw("\nUpgrade cost: `\u200b " + symbol + " " +  harvestUpgradeCost + " \u200b`");
                    maker.appendRaw("\nCurrent Funds: `\u200b " + symbol + " " +  currency + " \u200b`");
                    maker.withReactionBehavior("red_tick", ((add, reaction, u) -> {
                        //remove reactions
                        try {
                            maker.clearReactionBehaviors();
                        } catch (ConcurrentModificationException IGNORE) {
                        }
                        maker.sentMessage().getReactions().forEach(reactions -> maker.sentMessage().removeReaction(reactions));

                    }));
                    maker.withReactionBehavior("green_tick", ((add, reaction, u) -> {

                        //remove reactions
                        try {
                            maker.clearReactionBehaviors();
                        } catch (ConcurrentModificationException IGNORE) {
                        }
                        maker.sentMessage().getReactions().forEach(reactions -> maker.sentMessage().removeReaction(reactions));

                        maker.getHeader().clear();
                        int currencyTemp = currency;
                        int harvestUpgrades = ConfigHandler.getSetting(CurrentHarvestUpgradesConfig.class, user);
                        if (currencyTemp < harvestUpgradeCost) {
                            maker.appendRaw("You need `\u200b " + symbol + " " + (harvestUpgradeCost - currencyTemp) + " \u200b` more to buy this upgrade");
                        } else {
                            maker.appendRaw("Now level: ").appendEmbedLink(String.valueOf(++harvestUpgrades), "").appendRaw("\n\nFunds: `\u200b " + symbol + " " + (currencyTemp - harvestUpgradeCost) + " \u200b`");
                            ConfigHandler.setSetting(CurrentCurrencyConfig.class, user, (currencyTemp - harvestUpgradeCost));
                            ConfigHandler.setSetting(CurrentHarvestUpgradesConfig.class, user, harvestUpgrades);
                        }
                        maker.forceCompile().withoutReactionBehavior("green_tick").send();
                        maker.forceCompile().send();
                    }));
                }
                break;

            case "roaster":
                maker.getTitle().appendRaw("Roaster");
                maker.withThumb(EmoticonHelper.getEmoji("roaster").getImageUrl());
                if (ConfigHandler.getSetting(CurrentRoasterUpgradesConfig.class, user) == maxLevel && maxLevel < 3) {
                    maker.appendRaw("you're at the max level, you'll need to upgrade your house to store more powerful facilities");
                } else if (ConfigHandler.getSetting(CurrentRoasterUpgradesConfig.class, user) == maxLevel && maxLevel == 3) {
                    maker.appendRaw("you've reached the max level possible, your roaster is at its highest level");
                } else {
                    int roasterUpgradeCost = Integer.valueOf(Long.toString(Math.round(Math.pow(Double.parseDouble(String.valueOf(10 + ConfigHandler.getSetting(CurrentRoasterUpgradesConfig.class, user))), 3))));
                    maker.appendRaw("\nCurrent level: ").appendEmbedLink(String.valueOf(ConfigHandler.getSetting(CurrentRoasterUpgradesConfig.class, user)), "");
                    maker.appendRaw("\nUpgrade cost: `\u200b " + symbol + " " + roasterUpgradeCost + " \u200b`");
                    maker.appendRaw("\nCurrent Funds: `\u200b " + symbol + " " + currency + " \u200b`");
                    maker.withReactionBehavior("red_tick", ((add, reaction, u) -> {
                        //remove reactions
                        try {
                            maker.clearReactionBehaviors();
                        } catch (ConcurrentModificationException IGNORE) {
                        }
                        maker.sentMessage().getReactions().forEach(reactions -> maker.sentMessage().removeReaction(reactions));

                    }));
                    maker.withReactionBehavior("green_tick", ((add, reaction, u) -> {

                        //remove reactions
                        try {
                            maker.clearReactionBehaviors();
                        } catch (ConcurrentModificationException IGNORE) {
                        }
                        maker.sentMessage().getReactions().forEach(reactions -> maker.sentMessage().removeReaction(reactions));

                        maker.getHeader().clear();
                        int currencyTemp = currency;
                        int roasterUpgrades = ConfigHandler.getSetting(CurrentRoasterUpgradesConfig.class, user);

                        if (currency < roasterUpgradeCost) {
                            maker.appendRaw("You need `\u200b " + symbol + " " + (roasterUpgradeCost - currency) + " \u200b` more to buy this upgrade");
                        } else {
                            maker.appendRaw("Now level: ").appendEmbedLink(String.valueOf(++roasterUpgrades), "").appendRaw("\n\nFunds: `\u200b " + symbol + " " + (currency - roasterUpgradeCost) + " \u200b`");
                            ConfigHandler.setSetting(CurrentCurrencyConfig.class, user, (currency - roasterUpgradeCost));
                            ConfigHandler.setSetting(CurrentRoasterUpgradesConfig.class, user, roasterUpgrades);
                        }
                        maker.forceCompile().withoutReactionBehavior("green_tick").send();
                        maker.forceCompile().send();
                    }));
                }
                break;

            case "grinder":
                maker.getTitle().appendRaw("Grinder");
                maker.withThumb(EmoticonHelper.getEmoji("grinder").getImageUrl());
                if (ConfigHandler.getSetting(CurrentGrinderUpgradesConfig.class, user) == maxLevel && maxLevel < 3) {
                    maker.appendRaw("you're at the max level, you'll need to upgrade your house to store more powerful facilities");
                } else if (ConfigHandler.getSetting(CurrentGrinderUpgradesConfig.class, user) == maxLevel && maxLevel == 3) {
                    maker.appendRaw("you've reached the max level possible, your grinder is at its highest level");
                } else {
                    int grinderUpgradeCost = Integer.valueOf(Long.toString(Math.round(Math.pow(Double.parseDouble(String.valueOf(10 + ConfigHandler.getSetting(CurrentGrinderUpgradesConfig.class, user))), 3))));
                    maker.appendRaw("\nCurrent level: ").appendEmbedLink(String.valueOf(ConfigHandler.getSetting(CurrentGrinderUpgradesConfig.class, user)), "");
                    maker.appendRaw("\nUpgrade cost: `\u200b " + symbol + " " + grinderUpgradeCost + " \u200b`");
                    maker.appendRaw("\nCurrent Funds: `\u200b " + symbol + " " + currency + " \u200b`");
                    maker.withReactionBehavior("red_tick", ((add, reaction, u) -> {
                        //remove reactions
                        try {
                            maker.clearReactionBehaviors();
                        } catch (ConcurrentModificationException IGNORE) {
                        }
                        maker.sentMessage().getReactions().forEach(reactions -> maker.sentMessage().removeReaction(reactions));

                    }));
                    maker.withReactionBehavior("green_tick", ((add, reaction, u) -> {

                        //remove reactions
                        try {
                            maker.clearReactionBehaviors();
                        } catch (ConcurrentModificationException IGNORE) {
                        }
                        maker.sentMessage().getReactions().forEach(reactions -> maker.sentMessage().removeReaction(reactions));

                        maker.getHeader().clear();

                        int currencyTemp = currency;
                        int grinderUpgrades = ConfigHandler.getSetting(CurrentGrinderUpgradesConfig.class, user);

                        if (currencyTemp < grinderUpgradeCost) {
                            maker.appendRaw("You need `\u200b " + symbol + " " + (grinderUpgradeCost - currencyTemp) + " \u200b` more to buy this upgrade");
                        } else {
                            maker.appendRaw("Now level: ").appendEmbedLink(String.valueOf(++grinderUpgrades), "").appendRaw("\n\nFunds: `\u200b " + symbol + " " + (currencyTemp - grinderUpgradeCost) + " \u200b`");
                            ConfigHandler.setSetting(CurrentCurrencyConfig.class, user, (currencyTemp - grinderUpgradeCost));
                            ConfigHandler.setSetting(CurrentGrinderUpgradesConfig.class, user, grinderUpgrades);
                        }
                        maker.forceCompile().withoutReactionBehavior("green_tick").send();
                        maker.forceCompile().send();
                     }));
                }
                break;

            case "brewer":
                maker.getTitle().appendRaw("Brewer");
                maker.withThumb(EmoticonHelper.getEmoji("brewer").getImageUrl());
                if (ConfigHandler.getSetting(CurrentBrewerUpgradesConfig.class, user) == maxLevel && maxLevel < 3) {
                    maker.appendRaw("you're at the max level, you'll need to upgrade your house to store more powerful facilities");
                } else if (ConfigHandler.getSetting(CurrentBrewerUpgradesConfig.class, user) == maxLevel && maxLevel == 3) {
                    maker.appendRaw("you've reached the max level possible, your brewer is at its highest level");
                } else {
                    int brewerUpgradeCost = Integer.valueOf(Long.toString(Math.round(Math.pow(Double.parseDouble(String.valueOf(10 + ConfigHandler.getSetting(CurrentBrewerUpgradesConfig.class, user))), 3))));
                    maker.appendRaw("\nCurrent level: ").appendEmbedLink(String.valueOf(ConfigHandler.getSetting(CurrentBrewerUpgradesConfig.class, user)), "");
                    maker.appendRaw("\nUpgrade cost: `\u200b " + symbol + " " + brewerUpgradeCost + " \u200b`");
                    maker.appendRaw("\nCurrent Funds: `\u200b " + symbol + " " + currency + " \u200b`");
                    maker.withReactionBehavior("red_tick", ((add, reaction, u) -> {
                        //remove reactions
                        try {
                            maker.clearReactionBehaviors();
                        } catch (ConcurrentModificationException IGNORE) {
                        }
                        maker.sentMessage().getReactions().forEach(reactions -> maker.sentMessage().removeReaction(reactions));

                    }));
                    maker.withReactionBehavior("green_tick", ((add, reaction, u) -> {

                        //remove reactions
                        try {
                            maker.clearReactionBehaviors();
                        } catch (ConcurrentModificationException IGNORE) {
                        }
                        maker.sentMessage().getReactions().forEach(reactions -> maker.sentMessage().removeReaction(reactions));

                        maker.getHeader().clear();

                        int currencyTemp = currency;
                        int brewerUpgrades = ConfigHandler.getSetting(CurrentBrewerUpgradesConfig.class, user);
                        if (currencyTemp < brewerUpgradeCost) {
                            maker.appendRaw("You need `\u200b " + symbol + " " + (brewerUpgradeCost - currencyTemp) + " \u200b` more to buy this upgrade");
                        } else {
                            maker.appendRaw("Now level: ").appendEmbedLink(String.valueOf(++brewerUpgrades), "").appendRaw("\n\nFunds: `\u200b " + symbol + " " + (currencyTemp - brewerUpgradeCost) + " \u200b`");
                            ConfigHandler.setSetting(CurrentCurrencyConfig.class, user, (currencyTemp - brewerUpgradeCost));
                            ConfigHandler.setSetting(CurrentBrewerUpgradesConfig.class, user, brewerUpgrades);
                        }
                        maker.forceCompile().withoutReactionBehavior("green_tick").send();
                        maker.forceCompile().send();
                    }));
                }
                break;

            case "steeper":
                maker.getTitle().appendRaw("Steeper");
                maker.withThumb(EmoticonHelper.getEmoji("steeper").getImageUrl());
                if (ConfigHandler.getSetting(CurrentSteeperUpgradesConfig.class, user) == maxLevel && maxLevel < 3) {
                    maker.appendRaw("you're at the max level, you'll need to upgrade your house to store more powerful facilities");
                } else if (ConfigHandler.getSetting(CurrentSteeperUpgradesConfig.class, user) == maxLevel && maxLevel == 3) {
                    maker.appendRaw("you've reached the max level possible, your steeper is at its highest level");
                } else {
                    int steeperUpgradeCost = Integer.valueOf(Long.toString(Math.round(Math.pow(Double.parseDouble(String.valueOf(10 + ConfigHandler.getSetting(CurrentSteeperUpgradesConfig.class, user))), 3))));
                    maker.appendRaw("\nCurrent level: ").appendEmbedLink(String.valueOf(ConfigHandler.getSetting(CurrentSteeperUpgradesConfig.class, user)), "");
                    maker.appendRaw("\nUpgrade cost: `\u200b " + symbol + " " + steeperUpgradeCost + " \u200b`");
                    maker.appendRaw("\nCurrent Funds: `\u200b " + symbol + " " + currency + " \u200b`");
                    maker.withReactionBehavior("red_tick", ((add, reaction, u) -> {
                        //remove reactions
                        try {
                            maker.clearReactionBehaviors();
                        } catch (ConcurrentModificationException IGNORE) {
                        }
                        maker.sentMessage().getReactions().forEach(reactions -> maker.sentMessage().removeReaction(reactions));

                    }));
                    maker.withReactionBehavior("green_tick", ((add, reaction, u) -> {

                        //remove reactions
                        try {
                            maker.clearReactionBehaviors();
                        } catch (ConcurrentModificationException IGNORE) {
                        }
                        maker.sentMessage().getReactions().forEach(reactions -> maker.sentMessage().removeReaction(reactions));

                        maker.getHeader().clear();

                        int currencyTemp = currency;
                        int steeperUpgrades = ConfigHandler.getSetting(CurrentSteeperUpgradesConfig.class, user);
                        if (currencyTemp < steeperUpgradeCost) {
                            maker.appendRaw("You need `\u200b " + symbol + " " + (steeperUpgradeCost - currencyTemp) + " \u200b` more to buy this upgrade");
                        } else {
                            maker.appendRaw("Now level: ").appendEmbedLink(String.valueOf(++steeperUpgrades), "").appendRaw("\n\nFunds: `\u200b " + symbol + " " + (currencyTemp - steeperUpgradeCost) + " \u200b`");
                            ConfigHandler.setSetting(CurrentCurrencyConfig.class, user, (currencyTemp - steeperUpgradeCost));
                            ConfigHandler.setSetting(CurrentSteeperUpgradesConfig.class, user, steeperUpgrades);
                        }
                        maker.forceCompile().withoutReactionBehavior("green_tick").send();
                        maker.forceCompile().send();
                    }));
                }
                break;

            default:
                maker.appendRaw("You'll need to specify which part of the plantation it is you want to upgrade:\n`house, plants, roaster, grinder, brewer, steeper`");
                break;
        }
    }
}
