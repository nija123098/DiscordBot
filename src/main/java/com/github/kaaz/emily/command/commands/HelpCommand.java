package com.github.kaaz.emily.command.commands;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.anotations.Argument;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.util.EmoticonHelper;
import com.github.kaaz.emily.util.FormatHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * Made by nija123098 on 4/28/2017.
 */
public class HelpCommand extends AbstractCommand {
    public HelpCommand() {
        super("help", ModuleLevel.INFO, null, null, "Gives information about a command");
    }
    @Command
    public void command(@Argument AbstractCommand command, MessageMaker maker, User user, Guild guild){
        command = command.getHighCommand();
        if (!command.hasPermission(user, guild)) {
            maker.append("You don't have permission to look at that command!");
            return;
        }
        maker.appendRaw(EmoticonHelper.getChars("keyboard")).append("**Accessible Through:**").appendRaw("```\n");
        maker.appendRaw(command.getName() + "\n```\n" + EmoticonHelper.getChars("notepad_spiral") + "**Description:**\n```\n");
        maker.append(command.getHelp()).appendRaw("\n```\n" + EmoticonHelper.getChars("gear") + "**Usages:**\n```\n");
        maker.append(normalizeUsages(command.getUsages())).appendRaw("```");
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
        map.forEach((s, integer) -> builder.append(s.substring(0, integer)).append(FormatHelper.repeat(' ', distance.get() - integer + 2)).append(s.substring(integer)).append("\n"));
        return builder.toString();
    }
}
