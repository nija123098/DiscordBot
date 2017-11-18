package com.github.nija123098.evelyn.helping;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.economy.configs.LastMoneyUseConfig;


/**
 * Written by Soarnir 19/9/17
 */

public class TestCommand extends AbstractCommand {
    public TestCommand() {
        super("test", ModuleLevel.BOT_ADMINISTRATIVE, null, null, "test errythang");
    }

    @Command
    public void command(@Argument String arg, MessageMaker maker, User user) {
        String[] args = arg.split(" ");
        /*
        maker.appendRaw("```" +
                "\u200b       ,-.\n" +
                "\u200b    O /   `.\n" +
                "\u200b   <\\/      `.\n" +
                "\u200b    |*        `.\n" +
                "\u200b   / \\          `.\n" +
                "\u200b  /  /            `>3s,\n" +
                "\u200b--------.+" +
                "```").mustEmbed();
        */
        ConfigHandler.setSetting(LastMoneyUseConfig.class, user, "test");
        //ConfigHandler.setSetting(CurrentMoneyConfig.class, user, Integer.valueOf(arg));
        /*
        String[] args = arg.split(" ");
        switch (args[0]) {
            case "bean":
            case "beans":
                ConfigHandler.setSetting(CurrentBeansConfig.class, user, Integer.valueOf(args[1]));
                break;
            case "roast":
                ConfigHandler.setSetting(CurrentRoastedBeansConfig.class, user, Integer.valueOf(args[1]));
                break;
            case "grounds": case "ground":
                ConfigHandler.setSetting(CurrentGroundsConfig.class, user, Integer.valueOf(args[1]));
                break;
            case "coffee":
                ConfigHandler.setSetting(HasCoffeeConfig.class, user, Boolean.valueOf(args[1]));
                break;
            case "money":
                ConfigHandler.setSetting(CurrentMoneyConfig.class, user, Integer.valueOf(args[1]));
            default:
                break;
        }
        */
    }
}
