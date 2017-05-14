package com.github.kaaz.emily.command.commands;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.command.anotations.Argument;
import com.github.kaaz.emily.command.anotations.LaymanName;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;

import java.lang.reflect.Parameter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Made by nija123098 on 4/28/2017.
 */
public class HelpCommand extends AbstractCommand {
    public HelpCommand() {
        super("help", ModuleLevel.INFO, null, null, "Gives information about a command");
    }
    @Command
    public void command(@Argument AbstractCommand command, MessageMaker maker){
        List<Parameter> parameters = Stream.of(command.getParameters()).filter(parameter -> parameter.isAnnotationPresent(Argument.class)).collect(Collectors.toList());
        maker.append(command.getHelp()).append("\n\nArguments: \n");
        String argString;
        if (parameters.size() == 0){
            argString = "none";
        } else {
            StringBuilder builder = new StringBuilder();
            parameters.forEach(parameter -> builder.append(parameter.isAnnotationPresent(LaymanName.class) ? parameter.getAnnotation(LaymanName.class).value() : parameter.getType().getSimpleName()).append(parameter.getAnnotation(Argument.class).optional() ? " (optional)" : "").append(", "));
            argString = builder.toString();
            argString = argString.substring(0,argString.length() - 2);
        }
        maker.append(argString);
    }
}
