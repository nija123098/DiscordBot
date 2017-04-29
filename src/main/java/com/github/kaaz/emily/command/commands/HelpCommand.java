package com.github.kaaz.emily.command.commands;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.command.anotations.Convert;
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
    public void command(@Convert AbstractCommand command, MessageMaker maker){
        List<Class<?>> types = Stream.of(command.getParameters()).filter(parameter -> parameter.isAnnotationPresent(Convert.class)).map(Parameter::getType).collect(Collectors.toList());
        maker.append(command.getHelp()).append("\n\nArguments: \n");
        String argString;
        if (types.size() == 0){
            argString = "none";
        } else {
            StringBuilder builder = new StringBuilder();
            types.forEach(clazz -> builder.append(clazz.isAnnotationPresent(LaymanName.class) ? clazz.getAnnotation(LaymanName.class).value() : clazz.getSimpleName()).append(", "));
            argString = builder.toString();
            argString = argString.substring(0,argString.length() - 2);
        }
        maker.append(argString);
    }
}
