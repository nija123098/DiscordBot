package com.github.nija123098.evelyn.helping;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.exception.ArgumentException;
import com.github.nija123098.evelyn.util.Rand;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.operator.Operator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Made by nija123098 on 5/17/2017.
 */
public class CalculateCommand extends AbstractCommand {
    private static final Operator DIE_OPERATOR = new Operator("§", 2, true, 10) {
        @Override
        public double apply(double... doubles) {
            double total = 0;
            int actual = (int) doubles[1] - 1;
            for (int i = 0; i < doubles[0]; ++i)  total += Rand.getRand(actual + 1) + 1;
            return total;
        }
    };
    private static final Operator POW = new Operator("^", 2, true, 8) {
        @Override
        public double apply(double... doubles) {
            return Math.pow(doubles[0], doubles[1]);
        }
    };
    private static final Operator BITWISE_AND = new Operator("&", 2, true, 7) {
        @Override
        public double apply(double... doubles) {
            return (int) doubles[0] & (int) doubles[1];
        }
    };
    private static final Operator BITWISE_INCLUSIVE_OR = new Operator("|", 2, true, 6) {
        @Override
        public double apply(double... doubles) {
            return (int) doubles[0] | (int) doubles[1];
        }
    };
    private static final Operator AERITHMETIC_RIGHT_SHIFT = new Operator(">>", 2, true, 9) {
        @Override
        public double apply(double... doubles) {
            return ((int) doubles[0]) >> ((int) doubles[1]);
        }
    };
    private static final Operator LOGICAL_RIGHT_SHIFT = new Operator(">>>", 2, true, 9) {
        @Override
        public double apply(double... doubles) {
            return ((int) doubles[0]) >>> ((int) doubles[1]);
        }
    };
    private static final Operator LEFT_SHIFT = new Operator("<<", 2, true, 9) {
        @Override
        public double apply(double... doubles) {
            return ((int) doubles[0]) << ((int) doubles[1]);
        }
    };
    public static final List<Operator> OPERATORS = new ArrayList<>();
    static {
        Collections.addAll(OPERATORS, DIE_OPERATOR, BITWISE_AND, POW, BITWISE_INCLUSIVE_OR, AERITHMETIC_RIGHT_SHIFT, LOGICAL_RIGHT_SHIFT, LEFT_SHIFT);
    }
    public CalculateCommand() {
        super("calculate", ModuleLevel.HELPER, "eval", null, "Evaluate a math expression");
    }
    @Command
    public void command(MessageMaker maker, @Argument(info = "The thing to evaluate") String arg){
        maker.append(eval(arg) + "");
    }
    public static double eval(String arg){
        arg = arg.replace("d", "§").replace("D", "§");
        try{return new ExpressionBuilder(arg).operator(OPERATORS).build().evaluate();
        } catch (Exception e){
            throw new ArgumentException(e);
        }
    }
}
