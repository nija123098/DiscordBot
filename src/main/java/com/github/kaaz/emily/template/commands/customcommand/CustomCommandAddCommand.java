package com.github.kaaz.emily.template.commands.customcommand;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ContextRequirement;
import com.github.kaaz.emily.command.InvocationObjectGetter;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.exeption.ArgumentException;
import com.github.kaaz.emily.template.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Made by nija123098 on 8/11/2017.
 */
public class CustomCommandAddCommand extends AbstractCommand {
    private static final Map<String, Class<?>> APPROVED_ARGUMENT_TYPES;
    static {
        Set<Class<?>> classes = InvocationObjectGetter.getConversionTypes();
        APPROVED_ARGUMENT_TYPES = new HashMap<>(classes.size() + 1, 1);
        classes.forEach(clazz -> APPROVED_ARGUMENT_TYPES.put(clazz.getSimpleName().toLowerCase(), clazz));
    }
    public CustomCommandAddCommand() {
        super(CustomCommandCommand.class, "add", null, null, "make", "Adds a command, if adding arguments split them with a :");
    }
    @Command
    public void command(Guild guild, String s){
        if (s == null || s.isEmpty()) throw new ArgumentException("Please use the format <name> <text>");
        String name = s.split(" ")[0];
        s = s.substring(name.length() + 1);
        String[] split = s.split(":");
        String template;
        Class<?>[] args;
        if (split.length > 1){
            template = s.substring(split[0].length() + 1);
            String[] argSplit = split[0].split(" ");
            args = new Class<?>[argSplit.length];
            for (int i = 0; i < argSplit.length; i++) {
                Class<?> clazz = APPROVED_ARGUMENT_TYPES.get(argSplit[i].toLowerCase());
                if (clazz == null) throw new ArgumentException("Unrecognized argument type: " + argSplit[i] + " available: " + APPROVED_ARGUMENT_TYPES.keySet());
                args[i] = clazz;
            }
        }else {
            args = new Class<?>[0];
            template = s;
        }
        CustomCommandDefinition definition = new CustomCommandDefinition(name, new ContextRequirement[]{ContextRequirement.USER, ContextRequirement.SHARD, ContextRequirement.CHANNEL, ContextRequirement.GUILD, ContextRequirement.MESSAGE, ContextRequirement.STRING}, args);
        Template templ = new Template(template, definition);
        ConfigHandler.alterSetting(CustomCommandConfig.class, guild, commands -> {
            for (CustomCommand command : commands) if (command.getName().equals(name)) throw new ArgumentException("That command already exists!");
            commands.add(new CustomCommand(templ, name));
        });
        CustomCommandHandler.loadGuild(guild);
    }
}
