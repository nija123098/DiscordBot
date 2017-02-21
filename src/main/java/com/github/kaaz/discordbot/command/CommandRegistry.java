package com.github.kaaz.discordbot.command;

import org.reflections.Reflections;

import java.util.Set;

/**
 * Made by nija123098 on 2/20/2017.
 */
public class CommandRegistry {
    public static void loadCommands(){
        Reflections reflections = new Reflections("com.github.kaaz.discordbot.command.commands");
        Set<Class<? extends AbstractCommand>> classes = reflections.getSubTypesOf(AbstractCommand.class);

    }
}
