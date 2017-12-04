package com.github.nija123098.evelyn.command.commands;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ContextType;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.command.annotations.Context;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.configs.guild.GuildPrefixConfig;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.exception.DevelopmentException;
import com.github.nija123098.evelyn.exception.PermissionsException;
import com.github.nija123098.evelyn.util.EmoticonHelper;
import com.github.nija123098.evelyn.util.FormatHelper;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class HelpCommand extends AbstractCommand {
    public HelpCommand() {
        super("help", ModuleLevel.INFO, null, null, "Gives information about a command");
    }
    @Command
    public static void command(@Argument(optional = true, replacement = ContextType.NONE) AbstractCommand command, MessageMaker maker, User user, Channel channel, @Context(softFail = true) Guild guild, @Context(softFail = true) ModuleLevel levelSelection, String full){
        if (command == null) {
            maker.withColor(new Color(39, 209, 110));
            maker.mustEmbed();
            maker.getTitle().clear().appendRaw("I'll show you the following commands:\n");
            List<ModuleLevel> levels = new ArrayList<>();
            if (full.toLowerCase().contains("full")) Collections.addAll(levels, ModuleLevel.values());
            else if (levelSelection == null) Stream.of(ModuleLevel.values()).filter(level -> level.getDefaultRole().hasRequiredRole(user, guild)).findFirst().ifPresent(levels::add);
            else levels.add(levelSelection);
            if (levels.isEmpty()) throw new DevelopmentException("Shouldn't be possible");
            for (ModuleLevel level : levels){
                if (level == ModuleLevel.NONE || !level.getDefaultRole().hasRequiredRole(user, guild)) continue;
                List<AbstractCommand> commands = level.getCommands().stream().filter(AbstractCommand::isHighCommand).filter(c -> c.hasPermission(user, channel)).collect(Collectors.toList());
                if (!commands.isEmpty()) {
                    maker.appendRaw("\u200B\n" + level.getIcon() + " " + level.name() + "\n" + FormatHelper.makeTable(commands.stream().map(AbstractCommand::getName).collect(Collectors.toList()), 16, 3));
                    commands.clear();
                }
            }
            maker.getNote().clear().appendRaw("For more details about a command use ").appendRaw((guild == null ? "" : ConfigHandler.getSetting(GuildPrefixConfig.class, guild)) + "help <command>");
            ModuleLevel.getGeneralApproved(user, guild).stream().filter(level -> level != ModuleLevel.NONE).forEach(level -> maker.withReactionBehavior(level.getIconName(), (add, reaction, u) -> {
                if (!u.equals(user)) return;
                maker.getHeader().clear();
                maker.forceCompile();
                maker.clearFieldParts();
                command(null, maker, u, channel, guild, level, "");
                maker.send();
            }));
        } else {
            maker.withColor(new Color(39, 209, 110));
            maker.mustEmbed();
            maker.getNote().clear().appendRaw("<> indicates an argument, [] indicates an optional argument.  Do not use <> or [] in a command.");
            command = command.getHighCommand();
            maker.getTitle().clear().appendRaw(EmoticonHelper.getChars("bulb", false) + command.getName().toUpperCase());
            if (!command.hasPermission(user, channel)) {
                throw new PermissionsException("You don't have permission to look at that command!");
            }
            maker.appendRaw("\u200B\n" + EmoticonHelper.getChars("keyboard", false)).append("** Accessible Through:**");
            maker.appendRaw(FormatHelper.makeTable(new ArrayList<>(command.getNames()).stream().filter(s -> !s.contains("_")).collect(Collectors.toList())) + EmoticonHelper.getChars("notepad_spiral", false) + "** Description:**\n```MD\n");
            maker.appendRaw(command.getHelp()).appendRaw("```\n" + EmoticonHelper.getChars("gear", false) + "** Usages:**\n```MD\n");
            String[] strings = normalizeUsages(command.getUsages()).split("\n");
            for (int i = 0; i < strings.length; i++) {
                if (i % 3 == 1) maker.append(strings[i]);
                else maker.appendRaw(strings[i]);
                maker.appendRaw("\n" + (i == strings.length - 1 ? "\n" : ""));
            }
            maker.appendRaw("```\n");
            if (command.getExample() != null){
                maker.appendRaw(EmoticonHelper.getChars("question", false) + "**").append(" Examples:").appendRaw("**\n```MD\n").append(command.getExample()).appendRaw("\n```\n");
            }
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
        map.forEach((s, integer) -> builder.append(s.substring(0, integer)).append("\n").append(s.substring(integer)).append("\n"));
        return builder.toString();
    }
}
