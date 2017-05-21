package com.github.kaaz.emily.helping;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.anotations.Argument;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import net.objecthunter.exp4j.ExpressionBuilder;

/**
 * Made by nija123098 on 5/17/2017.
 */
public class CalculateCommand extends AbstractCommand {
    public CalculateCommand() {
        super("calculate", ModuleLevel.HELPER, "eval", null, "Evaluate a math expression");
    }
    @Command
    public void command(MessageMaker maker, @Argument(info = "The thing to evaluate") String arg){
        maker.append(String.valueOf(new ExpressionBuilder(arg).build().evaluate()));
    }
}
