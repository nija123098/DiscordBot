package com.github.kaaz.emily.template.commands.customcommand;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.template.CustomCommandHandler;
import com.github.kaaz.emily.util.FormatHelper;

import java.util.ArrayList;
import java.util.Set;

/**
 * Made by nija123098 on 8/11/2017.
 */
public class CustomCommandCommand extends AbstractCommand {
    public CustomCommandCommand() {
        super("customcommand", ModuleLevel.ADMINISTRATIVE, "command", null, "Shows a list of existing custom commands");
    }
    @Command
    public void command(MessageMaker maker, Guild guild){
        Set<String> strings = CustomCommandHandler.getCustomCommandNames(guild);
        if (strings.isEmpty()) maker.append("You haven't set up any custom commands yet!");
        else{
            maker.append("All custom commands:");
            maker.appendRaw(FormatHelper.makeTable(new ArrayList<>(strings)));
        }
    }
}
