package com.github.kaaz.discordbot.command;

import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * Made by nija123098 on 2/20/2017.
 */
public class CommandRegistry {
    private static final List<AbstractCommand> COMMANDS = new ArrayList<>();
    static {
        EnumSet.allOf(Module.class).forEach(module -> {
            Reflections reflections = new Reflections("com.github.kaaz.discordbot.command.commands." + module.name().toLowerCase());
            Set<Class<? extends AbstractCommand>> classes = reflections.getSubTypesOf(AbstractCommand.class);

        });
    }
}
