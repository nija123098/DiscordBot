package com.github.kaaz.discordbot.command;

import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Made by nija123098 on 2/20/2017.
 */
public class CommandRegistry {
    private static final List<AbstractCommand> COMMANDS = new ArrayList<>();
    static {
        EnumSet.allOf(ModuleLevel.class).forEach(moduleLevel -> {
            Reflections reflections = new Reflections("com.github.kaaz.discordbot.command.commands." + moduleLevel.name().replace("_", "").toLowerCase());
            List<AbstractCommand> commands = new ArrayList<>();
            reflections.getSubTypesOf(AbstractCommand.class).forEach(clazz -> {
                try {
                    commands.add(clazz.newInstance());
                } catch (Exception e){
                    System.out.println("Error initing config: " + clazz.getSimpleName());
                    e.printStackTrace();
                }
            });
            AtomicReference<AbstractSuperCommand> superCommand = new AtomicReference<>();
            commands.forEach(abstractCommand -> {
                if (abstractCommand instanceof AbstractSuperCommand) {
                    superCommand.set((AbstractSuperCommand) abstractCommand);
                }
            });
            if (superCommand.get() == null){
                System.out.println("Could not find super command for moduleLevel " + moduleLevel.name());
                return;
            }
            commands.forEach(abstractCommand -> {
                if (abstractCommand instanceof AbstractSubCommand){
                    ((AbstractSubCommand) abstractCommand).setSuperCommand(superCommand.get());
                }
            });
        });
    }
}
