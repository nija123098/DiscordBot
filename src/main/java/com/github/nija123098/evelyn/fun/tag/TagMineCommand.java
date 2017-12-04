package com.github.nija123098.evelyn.fun.tag;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

import java.util.Map;

import static com.github.nija123098.evelyn.config.ConfigHandler.getSetting;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class TagMineCommand extends AbstractCommand {
    public TagMineCommand() {
        super(TagCommand.class, "mine", null, null, null, "Shows all your tags in the guild");
    }

    @Command
    public void command(User user, Guild guild, MessageMaker maker) {
        Map<String, Tag> list = getSetting(TagConfig.class, guild);
        if (list.isEmpty()) maker.append("You don't have any tags.");
        else {
            maker.getTitle().append("Your tags");
            list.forEach((s, tag) -> {
                if (tag.getUser().equals(user)) maker.getNewListPart().appendRaw(tag.getName());
            });
        }
    }
}
