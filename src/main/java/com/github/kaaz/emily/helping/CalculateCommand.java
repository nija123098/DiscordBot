package com.github.kaaz.emily.helping;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.anotations.Argument;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.exeption.ArgumentException;
import com.github.kaaz.emily.util.Rand;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.operator.Operator;

/**
 * Made by nija123098 on 5/17/2017.
 */
public class CalculateCommand extends AbstractCommand {
    public static final Operator DIE_OPERATOR = new Operator("§", 2, true, 10) {
        @Override
        public double apply(double... doubles) {
            double total = 0;
            int actual = (int) doubles[1] - 1;
            for (int i = 0; i < doubles[0]; ++i)  total += Rand.getRand(actual) + 1;
            return total;
        }
    };
    public CalculateCommand() {
        super("calculate", ModuleLevel.HELPER, "eval", null, "Evaluate a math expression");
    }
    @Command
    public void command(MessageMaker maker, @Argument(info = "The thing to evaluate") String arg){
        maker.append(eval(arg) + "");
    }
    public static double eval(String arg){
        arg = arg.replace("d", "§").replace("D", "§");
        try{return new ExpressionBuilder(arg).operator(DIE_OPERATOR).build().evaluate();
        } catch (Exception e){
            throw new ArgumentException(e);
        }
    }
}
