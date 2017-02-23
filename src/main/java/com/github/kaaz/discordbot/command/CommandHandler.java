package com.github.kaaz.discordbot.command;

import com.github.kaaz.discordbot.util.Log;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Made by nija123098 on 2/20/2017.
 */
public class CommandHandler {
    private static final List<AbstractCommand> COMMANDS;
    static {
        Reflections reflections = new Reflections("com.github.kaaz.discordbot.command.commands");
        Set<Class<? extends AbstractSuperCommand>> supers = reflections.getSubTypesOf(AbstractSuperCommand.class);
        List<AbstractSuperCommand> superCommands = new ArrayList<>(supers.size());
        supers.forEach(clazz -> {
            AbstractSuperCommand superCommand;
            try {
                superCommand = clazz.newInstance();
            } catch (Exception e){
                Log.log("Exception while initing command: " + clazz.getSimpleName(), e);
                return;
            }
            superCommands.add(superCommand);
        });
        COMMANDS = new ArrayList<>(superCommands);
        Set<Class<? extends AbstractSubCommand>> subs = reflections.getSubTypesOf(AbstractSubCommand.class);
        Map<String, List<Class<? extends AbstractSubCommand>>> subMap = new HashMap<>(subs.size());
        subs.forEach(clazz -> {
            String pac = clazz.getPackage().toString();
            if (!subMap.containsKey(pac)){
                subMap.put(pac, new ArrayList<>());
            }
            subMap.get(pac).add(clazz);
        });
        superCommands.forEach(superCommand -> {
            List<Class<? extends AbstractSubCommand>> classes = subMap.get(superCommand.getClass().getPackage().toString());
            if (classes != null){
                classes.forEach(clazz -> {
                    AbstractSubCommand subCommand;
                    try {
                        subCommand = clazz.newInstance();
                    } catch (Exception e){
                        Log.log("Exception while initing command: " + clazz.getSimpleName(), e);
                        return;
                    }
                    COMMANDS.add(subCommand);
                    subCommand.setSuperCommand(superCommand);
                });
            }
        });
    }
}
