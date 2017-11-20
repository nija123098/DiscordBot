package com.github.nija123098.evelyn.helping;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.util.Log;
import org.apache.commons.lang3.StringEscapeUtils;


/**
 * Written by Soarnir 19/9/17
 */

public class TestCommand extends AbstractCommand {
    public TestCommand() {
        super("test", ModuleLevel.BOT_ADMINISTRATIVE, null, null, "test errythang");
    }

    @Command
    public void command(@Argument String arg, MessageMaker maker, User user) {
        //maker.appendRaw(String.valueOf(arg.length()));
        Log.log(StringEscapeUtils.escapeJava(arg));
        char codePointHigh = arg.charAt(0);
        char codePointLow = arg.charAt(1);
        maker.appendRaw("Pair: " + StringEscapeUtils.escapeJava(String.valueOf(codePointHigh)) + " " + StringEscapeUtils.escapeJava(String.valueOf(codePointLow)) + "\ncodePoint: ");


        maker.appendRaw(String.valueOf(Character.toCodePoint(codePointHigh, codePointLow)) + "\nIs unicode?: ");
        maker.appendRaw(String.valueOf(Character.toCodePoint(codePointHigh, codePointLow) > 0));
        //maker.appendRaw(EmoticonHelper.getChars(Character.getName(127850).toLowerCase(),false));
        //ConfigHandler.setSetting(LastCurrencyUseConfig.class, user, "testlol");
        //ConfigHandler.setSetting(LastHarvestUseConfig.class, user, "2017-11-16T01:46:07Z");
        //maker.appendRaw("Dxeo is pretty decent").mustEmbed().withColor(new Color(46, 204, 113));
        //throw new DevelopmentException("Lmao you got bamboozled");
        //ConfigHandler.setSetting(LastCurrencyUseConfig.class, user, "test");
        //ConfigHandler.setSetting(CurrentCurrencyConfig.class, user, Integer.valueOf(arg));
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
                ConfigHandler.setSetting(CurrentCurrencyConfig.class, user, Integer.valueOf(args[1]));
            default:
                break;
        }
        */
    }
}
