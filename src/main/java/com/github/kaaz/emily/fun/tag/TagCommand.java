package com.github.kaaz.emily.fun.tag;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.exeption.ArgumentException;
import com.github.kaaz.emily.util.FormatHelper;

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
            }else{
                ConfigHandler.alterSetting(TagConfig.class, guild, stringTagMap -> stringTagMap.put(split[0], new Tag(user.getID(), split[0], FormatHelper.trimFront(s.substring(split[0].length())))));
                maker.withOK();
            }
        }else{
            maker.append("Available tags:");
            ConfigHandler.getSetting(TagConfig.class, guild).forEach((s1, tag) -> maker.getNewListPart().appendRaw(s1));
        }
    }
}
