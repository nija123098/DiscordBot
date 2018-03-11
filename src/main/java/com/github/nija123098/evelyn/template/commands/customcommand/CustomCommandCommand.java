package com.github.nija123098.evelyn.template.commands.customcommand;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.configs.guild.GuildPrefixConfig;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.template.CustomCommandHandler;
import com.github.nija123098.evelyn.util.FormatHelper;

import java.util.ArrayList;
import java.util.Set;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class CustomCommandCommand extends AbstractCommand {
    public CustomCommandCommand() {
        super("customcommand", ModuleLevel.ADMINISTRATIVE, "command", null, "Shows a list of existing custom commands");
    }
    Guild temp;
    @Command
    public void command(MessageMaker maker, Guild guild) {
        temp = guild;
        Set<String> strings = CustomCommandHandler.getCustomCommandNames(guild);
        if (strings.isEmpty()) maker.append("You haven't set up any custom commands yet!");
        else{
            maker.append("All custom commands:");
            maker.appendRaw(FormatHelper.makeTable(new ArrayList<>(strings)));
        }
    }

    @Override
    public String getExample() {
        return "#  customcommand add rickroll https://youtu.be/dQw4w9WgXcQ // Creates a custom command called `rickroll`, use " + ConfigHandler.getSetting(GuildPrefixConfig.class, temp);
    }
}
