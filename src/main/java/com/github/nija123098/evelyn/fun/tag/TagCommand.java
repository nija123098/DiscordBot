package com.github.nija123098.evelyn.fun.tag;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.exeption.ArgumentException;
import com.github.nija123098.evelyn.util.FormatHelper;

/**
 * Made by nija123098 on 5/30/2017.
 */
public class TagCommand extends AbstractCommand {
    public TagCommand() {
        super("tag", ModuleLevel.FUN, "tags, t", null, "Tags");
    }
    @Command
    public void command(Guild guild, MessageMaker maker, User user, String s){
        if (s != null){
            String[] split = s.split(" ");
            if (split.length == 1){
                Tag t = ConfigHandler.getSetting(TagConfig.class, guild).get(s);
                if (t == null) throw new ArgumentException("No tag called: " + s);
                maker.appendRaw(t.getContent());
            }else ConfigHandler.alterSetting(TagConfig.class, guild, stringTagMap -> stringTagMap.put(split[0], new Tag(user.getID(), split[0], FormatHelper.trimFront(s.substring(split[0].length())))));
        }else{
            maker.append("Available tags:");
            ConfigHandler.getSetting(TagConfig.class, guild).forEach((s1, tag) -> maker.getNewListPart().appendRaw(s1));
        }
    }

    @Override
    protected String getLocalUsages() {
        return "tag <name> <text> // Create your own tag!";
    }
}
