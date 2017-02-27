package com.github.kaaz.discordbot.command;

import com.github.kaaz.discordbot.util.Log;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Made by nija123098 on 2/20/2017.
 */
public class CommandHandler {
    private static final Map<String, AbstractCommand>[] SORTED_COMMANDS;
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
        Set<Class<? extends AbstractSubCommand>> subs = reflections.getSubTypesOf(AbstractSubCommand.class);
        Map<String, List<Class<? extends AbstractSubCommand>>> subMap = new HashMap<>(subs.size());
        subs.forEach(clazz -> {
            String pac = clazz.getPackage().toString();
            if (!subMap.containsKey(pac)){
                subMap.put(pac, new ArrayList<>());
            }
            subMap.get(pac).add(clazz);
        });
        List<AbstractCommand> commands = new ArrayList<>(superCommands.size() + subs.size());
        commands.addAll(superCommands);
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
                    commands.add(subCommand);
                    subCommand.setSuperCommand(superCommand);
                });
            }
        });
        Map<String, AbstractCommand>[] sortedCommands = new Map[7];
        for (int i = 0; i < sortedCommands.length; i++) {
            sortedCommands[i] = new HashMap<>();// no modifications will be made after this init
        }
        commands.forEach(abstractCommand -> abstractCommand.getAliases().forEach(ali -> {
            String[] strings = ali.split(" ");
            AbstractCommand repeat = sortedCommands[strings.length].get(ali);
            if (repeat == null){
                sortedCommands[strings.length].put(ali, abstractCommand);
            } else {
                Log.log("Duplicate alias found: " + abstractCommand.getClass().getName() + " " + repeat.getClass().getName() + " " + ali);
            }
        }));
        int high = 0;
        for (int i = sortedCommands.length - 1; i > 0; --i) {
            if (!sortedCommands[i].isEmpty()){
                high = i;
                break;
            }
        }
        SORTED_COMMANDS = new Map[high];
        for (int i = 1; i < high; i++) {// i = 1, no command name ""
            if (!sortedCommands[i].isEmpty()){
                SORTED_COMMANDS[i] = sortedCommands[i];
            }// could probably remake the map with the precise count of elements
        }
    }
    public static Triple<Boolean, String, AbstractCommand> getCommand(String commandIn){
        String commandReduced;
        {
            char[] original = commandIn.toCharArray();
            char[] reducedChars = new char[original.length];
            boolean hadSpace = true;
            int index = commandIn.indexOf("  ") - 3;
            if (index != -4){
                for (int i = index; i < reducedChars.length; ++i) {
                    if (original[i] == ' '){
                        if (!hadSpace){
                            hadSpace = true;
                            reducedChars[++index] = original[i];
                        }
                    } else {
                        reducedChars[++index] = original[i];
                        hadSpace = false;
                    }
                }
            }
            commandReduced = new String(reducedChars).substring(1, index + 1);
        }
        String[] strings = commandReduced.split(" ");
        String n;
        if (strings.length > SORTED_COMMANDS.length){
            strings = Arrays.copyOfRange(strings, 0, SORTED_COMMANDS.length);
        }
        AbstractCommand command;
        for (int i = strings.length - 1; i > - 1; --i) {
            n = "";
            for (int j = strings.length - i - 1; j > - 1; --j) {
                n += strings[j];
            }
            command = SORTED_COMMANDS[i].get(n);
            if (command != null){
                for (String string : strings) {
                    commandIn = commandIn.substring(string.length());
                    for (int j = 0; j < commandIn.length(); j++) {
                        if (commandIn.charAt(j) != ' '){
                            commandIn = commandIn.substring(j);
                        }
                    }
                }
                return new ImmutableTriple<>(command.getEmoticonAliases().contains(n), commandIn, command);
            }
        }
        return null;
    }
}
