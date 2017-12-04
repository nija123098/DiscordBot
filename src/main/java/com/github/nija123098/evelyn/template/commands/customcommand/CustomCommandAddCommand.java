package com.github.nija123098.evelyn.template.commands.customcommand;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ContextRequirement;
import com.github.nija123098.evelyn.command.InvocationObjectGetter;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.exception.ArgumentException;
import com.github.nija123098.evelyn.template.*;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class CustomCommandAddCommand extends AbstractCommand {
    private static final Map<String, Class<?>> APPROVED_ARGUMENT_TYPES;
    static {
        APPROVED_ARGUMENT_TYPES = InvocationObjectGetter.getConversionTypes().stream().collect(Collectors.toMap(clazz -> clazz.getSimpleName().toLowerCase(), Function.identity()));
    }
    public CustomCommandAddCommand() {
        super(CustomCommandCommand.class, "add", null, null, "make", "Adds a command, if adding arguments split them with a ;");
    }
    @Command
    public void command(Guild guild, String s){
        if (s == null || s.isEmpty()) throw new ArgumentException("Please use the format <name> <text>");
        String name = s.split(" ")[0];
        s = s.substring(name.length() + 1);
        String[] split = s.split(";");
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
