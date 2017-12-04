package com.github.nija123098.evelyn.fun.tag;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.exception.ArgumentException;

import static com.github.nija123098.evelyn.command.ModuleLevel.FUN;
import static com.github.nija123098.evelyn.config.ConfigHandler.alterSetting;
import static com.github.nija123098.evelyn.config.ConfigHandler.getSetting;
import static com.github.nija123098.evelyn.util.FormatHelper.trimFront;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class TagCommand extends AbstractCommand {
    public TagCommand() {
        super("tag", FUN, "tags, t", null, "Tags");
    }

    @Command
    public void command(Guild guild, MessageMaker maker, User user, String s) {
        if (s != null) {
            String[] split = s.split(" ");
            if (split.length == 1) {
                Tag t = getSetting(TagConfig.class, guild).get(s);
                if (t == null) throw new ArgumentException("No tag called: " + s);
                maker.appendRaw(t.getContent());
            } else
                alterSetting(TagConfig.class, guild, stringTagMap -> stringTagMap.put(split[0], new Tag(user.getID(), split[0], trimFront(s.substring(split[0].length())))));
        } else {
            maker.append("Available tags:");
            getSetting(TagConfig.class, guild).forEach((s1, tag) -> maker.getNewListPart().appendRaw(s1));
        }
    }

    @Override
    protected String getLocalUsages() {
        return "#  tag <name> <text> // Create your own tag!";
    }
}
