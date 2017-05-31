package com.github.kaaz.emily.fun.tag;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.User;

/**
 * Made by nija123098 on 5/30/2017.
 */
public class TagMineCommand extends AbstractCommand {
    public TagMineCommand() {
        super(TagCommand.class, "mine", null, null, null, "Shows all your tags in the guild");
    }
    @Command
    public void command(User user, Guild guild, MessageMaker maker){
        maker.append("Your tags:\n").appendRaw("```");
        ConfigHandler.getSetting(TagConfig.class, guild).forEach((s, tag) -> {
            if (tag.getUser().equals(user)) maker.getNewListPart().appendRaw(tag.getName());
        });
        maker.append("```");
    }
}
