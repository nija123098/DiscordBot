package com.github.kaaz.emily.fun;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ContextType;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.anotations.Argument;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.helping.CalculateCommand;
import com.github.kaaz.emily.util.Rand;

/**
 * Made by nija123098 on 5/25/2017.
 */
public class RollCommand extends AbstractCommand {
    public RollCommand() {
        super("roll", ModuleLevel.FUN, "dice, rng", "game_die", "For if you ever need a random number");
    }// this should have a chance of rick rolling you
    @Command
    public void command(@Argument(optional = true, replacement = ContextType.NONE) Integer first, @Argument(optional = true, replacement = ContextType.NONE) Integer second, String arg, MessageMaker maker){
        int value;
        if (first != null){
            if (second == null){
                value = Rand.getRand(first);
            }else{
                value = Rand.getRand(second - first) + first;
            }
        }else if (arg.isEmpty()){
            value = Rand.getRand(5) + 1;
        }else{
            value = (int) CalculateCommand.eval(arg);
        }
        maker.append(value + "");
    }
}
