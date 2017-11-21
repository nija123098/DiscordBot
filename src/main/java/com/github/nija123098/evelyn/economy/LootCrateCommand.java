package com.github.nija123098.evelyn.economy;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.command.annotations.Context;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.economy.configs.LootCrateConfig;
import com.github.nija123098.evelyn.exeption.ArgumentException;

import java.util.concurrent.TimeUnit;

/**
 * Written by Dxeo 21/11/2017
 */

public class LootCrateCommand extends AbstractCommand {

    //constructor
    public LootCrateCommand() {
        super("lootcrate", ModuleLevel.ECONOMY, null, null, "fish for shtuff");
    }


    @Command
    public void command(@Context(softFail = true) Guild guild, User user, MessageMaker maker) throws InterruptedException {

        //save loot crate symbol
        String frame_symbol = "\uD83D\uDCE6";
        String crate_symbol = "\uD83C\uDF81";

        //save user loot crate amount
        int userCrates = ConfigHandler.getSetting(LootCrateConfig.class, user);

        //if user has crates
        if (userCrates < 1) {
            throw new ArgumentException("You have: `\u200B " + crate_symbol + " " + userCrates + " \u200B`");
        }

        //configure message maker
        maker.withAutoSend(false);
        maker.mustEmbed();

        //display the first frame if there are crates
        maker.appendRaw("```" + frame_symbol + " @" + user.getDisplayName(guild) + " " + frame_symbol + "\n");
        maker.appendRaw("════════════════════════════════════════\n\n");
        maker.appendRaw("           Opening loot crate...\n\n               ✨ \uD83C\uDF81 ✨\n\n");
        maker.appendRaw("════════════════════════════════════════\n");
        maker.appendRaw(" Crates: " + crate_symbol + " " + userCrates + "```" );
        maker.send();

        //clear the maker
        maker.getHeader().clear();

        //subtract crate
        userCrates = ConfigHandler.getSetting(LootCrateConfig.class, user);
        ConfigHandler.setSetting(LootCrateConfig.class, user,  (userCrates - 1));
        userCrates -= 1;

        //dispense reward
        String reward_symbol = "✨";
        int reward = 500;

        //display the second frame after delay
        TimeUnit.SECONDS.sleep(2);
        maker.appendRaw("```" + frame_symbol + " @" + user.getDisplayName(guild) + " " + frame_symbol + "\n");
        maker.appendRaw("════════════════════════════════════════\n\n");
        maker.appendRaw("        Congratulations you got:\n\n");
        maker.appendRaw("               \uD83C\uDF89 " + reward_symbol + " \uD83C\uDF89\n\n");
        maker.appendRaw("════════════════════════════════════════\n");
        maker.appendRaw(" Crates: " + crate_symbol + " " + userCrates + "  Loot: " + reward_symbol + " " + reward + "```");

        //add reaction for repeating the command
        maker.withReactionBehavior("package", ((add, reaction, u) -> {

            //save user loot crate amount
            int mUserCrates = ConfigHandler.getSetting(LootCrateConfig.class, user);

            //print the first frame
            maker.appendRaw("```" + frame_symbol + " @" + user.getDisplayName(guild) + " " + frame_symbol + "\n");
            maker.appendRaw("════════════════════════════════════════\n\n");
            maker.appendRaw("           Opening loot crate...\n\n               ✨ \uD83C\uDF81 ✨\n\n");
            maker.appendRaw("════════════════════════════════════════\n");
            maker.appendRaw(" Crates: " + crate_symbol + " " + mUserCrates + "```" );
            maker.send();

            //clear the maker
            maker.getHeader().clear();

            try {
                command(guild,user,maker);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }));
        maker.send();
    }
}