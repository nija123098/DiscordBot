package com.github.kaaz.emily.fun.tag;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ContextType;
import com.github.kaaz.emily.command.annotations.Argument;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.exeption.ArgumentException;

/**
 * Made by nija123098 on 5/30/2017.
 */
public class TagDeleteCommand extends AbstractCommand {
    public TagDeleteCommand() {
        super(TagCommand.class, "delete", "deleteuser", null, null, "Deletes a tag");
    }
    @Command
    public void command(@Argument(optional = true, replacement = ContextType.NONE) User user, @Argument String s, Guild guild, MessageMaker maker){
        if (user == null && s.isEmpty()) throw new ArgumentException("Chose either a user or a tag for tag deletion");
        if (s.contains(" ")) throw new ArgumentException("Tags are all one name");
        if (user == null){
            ConfigHandler.alterSetting(TagConfig.class, guild, stringTagMap -> {
                if (stringTagMap.containsKey(s)) stringTagMap.remove(s);
                else throw new ArgumentException("No such tag called: " + s);
            });
        } else {
            if (!TagConfig.deleteOfUser(user, guild)) maker.append("That user doesn't own any tags");
        }
    }
}
