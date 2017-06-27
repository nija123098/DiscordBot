package com.github.kaaz.emily.command.commands;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ContextType;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Argument;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.Channel;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.util.EmoticonHelper;
import com.github.kaaz.emily.util.FormatHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
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
    public void command(@Argument(optional = true, replacement = ContextType.NONE) AbstractCommand command, MessageMaker maker, User user, Channel channel){
        if (command == null) {
            maker.append("I'll show you the following commands:\n");
            for (ModuleLevel level : ModuleLevel.values()){
                if (level == ModuleLevel.NONE) continue;
                List<AbstractCommand> commands = level.getCommands().stream().filter(AbstractCommand::isHighCommand).filter(c -> c.hasPermission(user, channel)).collect(Collectors.toList());
                if (!commands.isEmpty()) {
                    maker.appendRaw(level.getIcon() + " " + level.name() + "\n" + FormatHelper.makeTable(commands.stream().map(AbstractCommand::getName).collect(Collectors.toList())));
                    commands.clear();
                }
            }
            maker.append("For more details about a command use ").appendRaw("!help <command>");
        } else {
            command = command.getHighCommand();
            if (!command.hasPermission(user, channel)) {
                maker.append("You don't have permission to look at that command!");
                return;
            }
            maker.appendRaw(EmoticonHelper.getChars("keyboard")).append("**Accessible Through:**").appendRaw("```\n");
            maker.appendRaw(command.getName() + "\n```\n" + EmoticonHelper.getChars("notepad_spiral") + "**Description:**\n```\n");
            maker.append(command.getHelp()).appendRaw("\n```\n" + EmoticonHelper.getChars("gear") + "**Usages:**\n```\n");
            maker.append(normalizeUsages(command.getUsages())).appendRaw("```");
            maker.append("\n<> indicates an argument, [] indicates an optional argument");
        }
    }
    private static String normalizeUsages(String help){
        AtomicInteger distance = new AtomicInteger();
        Map<String, Integer> map = new HashMap<>();
        Stream.of(help.split("\n")).forEach(row -> {
            int d = Math.max(0, row.indexOf("//"));
            map.put(row, d);
            if (d > distance.get()) distance.set(d);
        });
        StringBuilder builder = new StringBuilder();
        map.forEach((s, integer) -> builder.append(s.substring(0, integer)).append("\n").append(s.substring(integer)).append("\n\n"));
        return builder.toString();
    }
}
