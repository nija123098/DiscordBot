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

        //configure message maker
        maker.withAutoSend(false);
        maker.mustEmbed();

        if (userCrates == 0){
            //display the first frame if no crates
            maker.appendRaw("```" + frame_symbol + " @" + user.getDisplayName(guild) + " " + frame_symbol + "\n");
            maker.appendRaw("════════════════════════════════════════\n");
            maker.appendRaw(" Unfortunately you do not have\n any loot crates :/\n");
            maker.appendRaw("════════════════════════════════════════\n");
            maker.appendRaw(" Crate: " + crate_symbol + " " + userCrates + "```" );
            maker.send();

        } else {

            //display the first frame if there are crates
            maker.appendRaw("```" + frame_symbol + " @" + user.getDisplayName(guild) + " " + frame_symbol + "\n");
            maker.appendRaw("════════════════════════════════════════\n");
            maker.appendRaw(" first\n");
            maker.appendRaw("════════════════════════════════════════\n");
            maker.appendRaw(" Crate: " + crate_symbol + " " + userCrates + "```" );
            maker.send();

            //clear the maker
            maker.getHeader().clear();

            //subtract crate
            userCrates = ConfigHandler.getSetting(LootCrateConfig.class, user);
            ConfigHandler.setSetting(LootCrateConfig.class, user,  (userCrates - 1));
            userCrates -= 1;

            //dispense reward

            //display the second frame after delay
            TimeUnit.SECONDS.sleep(2);
            maker.appendRaw("```" + frame_symbol + " @" + user.getDisplayName(guild) + " " + frame_symbol + "\n");
            maker.appendRaw("════════════════════════════════════════\n");
            maker.appendRaw(" second\n");
            maker.appendRaw("════════════════════════════════════════\n");
            maker.appendRaw(" Crate: " + crate_symbol + " " + userCrates + "```" );

            //add reaction for repeating the command
            int finalUserCrates = userCrates;
            maker.withReactionBehavior("package", ((add, reaction, u) -> {

                //print the first frame
                maker.appendRaw("```" + frame_symbol + " @" + user.getDisplayName(guild) + " " + frame_symbol + "\n");
                maker.appendRaw("════════════════════════════════════════\n");
                maker.appendRaw("first\n");
                maker.appendRaw("════════════════════════════════════════\n");
                maker.appendRaw(" Crate: " + crate_symbol + " " + finalUserCrates + "```" );
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
}